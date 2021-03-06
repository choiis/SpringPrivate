package com.singer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.singer.vo.SV02Vo;

@Repository("sv02Dao")
public class SV02Dao extends SuperDao {
	private static final String namespace = "com.singer.mappers.SV02";

	public int insertSV02Vo(List<SV02Vo> list) throws Exception {
		return insert(namespace + ".insert", list);
	}

	public int updateSV02Vo(SV02Vo sv02Vo) throws Exception {
		return insert(namespace + ".update", sv02Vo);
	}

	@SuppressWarnings("unchecked")
	public List<SV02Vo> selectSV02Vo(SV02Vo sv02Vo) throws Exception {
		return (List<SV02Vo>) selectList(namespace + ".select", sv02Vo);
	}

	public int selectCnt(SV02Vo sv02Vo) throws Exception {
		return selectCnt(namespace + ".selectTot", sv02Vo);
	}

	public int deleteSV02Vo(SV02Vo sv02Vo) throws Exception {
		return delete(namespace + ".delete", sv02Vo);
	}

	public int insertSV03Vo(List<SV02Vo> list) throws Exception {
		return insert(namespace + ".insertSv03Vo", list);
	}
}
