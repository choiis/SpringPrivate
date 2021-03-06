package com.singer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.singer.exception.AppException;
import com.singer.exception.ExceptionMsg;
import com.singer.util.S3Util;
import com.singer.common.CommonUtil;
import com.singer.common.Constants.RESULT_CODE;
import com.singer.common.DateUtil;
import com.singer.dao.SR01Dao;
import com.singer.dao.SR02Dao;
import com.singer.dao.SR03Dao;
import com.singer.vo.SR01Vo;
import com.singer.vo.SR03Vo;

import lombok.Cleanup;

@Service("sr01Service")
public class SR01ServiceImpl implements SR01Service {

	@Resource(name = "sr01Dao")
	private SR01Dao sr01Dao;

	@Resource(name = "sr02Dao")
	private SR02Dao sr02Dao;

	@Resource(name = "sr03Dao")
	private SR03Dao sr03Dao;

	@Inject
	S3Util s3Util;

	@Resource(name = "properties")
	private Properties properties;

	@Transactional
	@Override
	public int insertSR01Vo(SR01Vo sr01Vo, MultipartHttpServletRequest request, String sessionid) throws Exception {
		if (CommonUtil.isNull(sr01Vo.getTitle())) {
			throw new AppException(ExceptionMsg.EXT_MSG_INPUT_1);
		}
		if (CommonUtil.isNull(sr01Vo.getText())) {
			throw new AppException(ExceptionMsg.EXT_MSG_INPUT_2);
		}
		if (CommonUtil.isNull(sr01Vo.getMarkertitle())) {
			throw new AppException(ExceptionMsg.EXT_MSG_INPUT_6);
		}
		if (sr01Vo.getGrade() < 0 || sr01Vo.getGrade() > 5) {
			throw new AppException(ExceptionMsg.EXT_MSG_INPUT_7);
		}

		sr01Vo.setUserid(sessionid);
		sr01Vo.setRegdate(DateUtil.getTodayTime());

		sr01Dao.insertSR01Vo(sr01Vo);
		sr02Dao.insertSR02Vo(sr01Vo);
		List<MultipartFile> fileList = request.getFiles("file");
		int idx = 0;
		ArrayList<SR01Vo> arrayList = new ArrayList<>();
		String today = DateUtil.getToday();
		String path = properties.getProperty("global.ftp.path");
		for (MultipartFile photo : fileList) {
			if (photo.getSize() == 0) {
				continue;
			}
			String originalFilename = photo.getOriginalFilename();
			if (!CommonUtil.chkIMGFile(originalFilename)) {
				throw new AppException(ExceptionMsg.EXT_MSG_INPUT_4);
			}
			SR01Vo sr01Vos = new SR01Vo();
			sr01Vos.setSeq(sr01Vo.getSeq());
			sr01Vos.setIdx(idx++);
			sr01Vos.setRegdate(today);
			StringBuilder sb = new StringBuilder("rphoto/");
			sb.append(sr01Vo.getSeq() + "_" + today + "_" + idx + CommonUtil.getExtensionName(originalFilename));
			sr01Vos.setPhotopath(sb.toString());
			arrayList.add(sr01Vos);

			File file = new File(path + "/" + sb.toString());

			@Cleanup
			InputStream in = photo.getInputStream();

			@Cleanup
			FileOutputStream fos = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int read;

			while ((read = in.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}

			s3Util.putS3File(sb.toString(), file);
		}
		sr01Dao.insertImage(arrayList);
		return sr01Vo.getSeq() > 0 ? 1 : 0;
	}

	@Override
	public List<SR01Vo> selectSR01Vo(SR01Vo sr01Vo) throws Exception {
		return sr01Dao.selectSR01Vo(sr01Vo);
	}

	@Override

	public List<SR01Vo> selectFindSR01Vo(SR01Vo sr01Vo) throws Exception {
		if (CommonUtil.isNull(sr01Vo.getFindText())) {
			throw new AppException(ExceptionMsg.EXT_MSG_INPUT_10);
		} else if (sr01Vo.getSelection() == 1) { // �젣紐⑹쑝濡� 寃��깋
			sr01Vo.setTitle(sr01Vo.getFindText());
		} else if (sr01Vo.getSelection() == 2) { // �븘�씠�뵒濡� 寃��깋
			sr01Vo.setUserid(sr01Vo.getFindText());
		} else {
			throw new AppException(ExceptionMsg.EXT_MSG_INPUT_11);
		}

		return sr01Dao.selectFindSR01Vo(sr01Vo);
	}

	@Transactional
	@Override
	public SR01Vo selectOneSR01Vo(SR01Vo sr01Vo, String userid) throws Exception {

		sr01Dao.clickSR01Vo(sr01Vo);

		sr01Vo.setUserid(userid);
		sr01Vo = sr01Dao.selectOneSR01Vo(sr01Vo);
		if (!CommonUtil.isNull(sr01Vo)) {
			if (userid.equals(sr01Vo.getUserid())) {
				sr01Vo.setDeleteYn(true);
			}
		}
		return sr01Vo;
	}

	@Override
	public int updateSR01Vo(SR01Vo sr01Vo) throws Exception {
		return sr01Dao.updateSR01Vo(sr01Vo);
	}

	@Transactional
	@Override
	public SR01Vo likeSR01Vo(SR01Vo sr01Vo, String sessionid) throws Exception {
		sr01Dao.likeSR01Vo(sr01Vo);

		sr01Vo.setSessionid(sessionid);
		sr01Vo.setDatelog(DateUtil.getTodayTime());

		sr01Dao.likelogSR01Vo(sr01Vo);

		sr01Vo.setResult(RESULT_CODE.SUCCESS.getValue());
		return sr01Vo;
	}

	@Transactional
	@Override
	public SR01Vo hateSR01Vo(SR01Vo sr01Vo, String sessionid) throws Exception {
		sr01Dao.hateSR01Vo(sr01Vo);

		sr01Vo.setSessionid(sessionid);
		sr01Vo.setDatelog(DateUtil.getTodayTime());

		sr01Dao.hatelogSR01Vo(sr01Vo);

		sr01Vo.setResult(RESULT_CODE.SUCCESS.getValue());
		return sr01Vo;
	}

	@Transactional
	@Override
	public int deleteSR01Vo(SR01Vo sr01Vo) throws Exception {

		SR03Vo sr03Vo = new SR03Vo();
		sr03Vo.setSeq(sr01Vo.getSeq());

		sr03Dao.delete_seqSR03Vo(sr03Vo);

		return sr01Dao.deleteSR01Vo(sr01Vo);
	}

	@Override
	public InputStream selectPhoto(SR01Vo sr01Vo) throws Exception {
		return s3Util.getS3FileStream(sr01Dao.selectPhoto(sr01Vo));
	}

}
