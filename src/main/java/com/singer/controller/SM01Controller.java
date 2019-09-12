package com.singer.controller;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.singer.service.SM01Service;
import com.singer.vo.SM01Vo;

@Controller("sM01Controller")
public class SM01Controller {

	private final Log log = LogFactory.getLog(SM01Controller.class);

	@Resource(name = "sm01Service")
	private SM01Service sm01Service;

	@RequestMapping(value = "/sm01joinPage", method = RequestMethod.GET)
	public ModelAndView joinPage() {

		ModelAndView model = new ModelAndView("/join");

		return model;
	}

	@RequestMapping(value = "/sm01", method = RequestMethod.POST)
	public ResponseEntity<SM01Vo> insertSM01Vo(@ModelAttribute("SM01Vo") SM01Vo sm01Vo,
			MultipartHttpServletRequest request) throws Exception {

		sm01Service.insertSM01Vo(sm01Vo, request);

		return new ResponseEntity<SM01Vo>(sm01Vo, HttpStatus.OK);
	}

	@RequestMapping(value = "/sm01page", method = RequestMethod.GET)
	public ModelAndView showSM01() {
		ModelAndView model = new ModelAndView("/sm01list");
		return model;
	}

	@ResponseBody
	@RequestMapping(value = "/sm01/{nowPage}", method = RequestMethod.GET)
	public ResponseEntity<SM01Vo> selectSM01Vo(@ModelAttribute SM01Vo sm01Vo) throws Exception {
		log.debug("sm01Vo : " + sm01Vo);

		List<SM01Vo> list = sm01Service.selectSM01Vo(sm01Vo);
		sm01Vo.setList(list);

		log.debug("list : " + list);
		return new ResponseEntity<SM01Vo>(sm01Vo, HttpStatus.OK);
	}

	@RequestMapping(value = "/sm01update", method = RequestMethod.POST)
	public ModelAndView upateSM01Vo(@ModelAttribute SM01Vo sm01Vo, MultipartHttpServletRequest request,
			HttpSession session) throws Exception {
		log.debug("sm01Vo : " + sm01Vo);

		String userid = (String) session.getAttribute("userid");

		sm01Vo = sm01Service.updateSM01Vo(sm01Vo, request, userid);

		ModelAndView model = new ModelAndView("/sm01update");
		model.addObject("sm01Vo", sm01Vo);

		return model;
	}

	@RequestMapping(value = "/sm01change", method = RequestMethod.GET)
	public ModelAndView selectOneSM01Vo(HttpSession session) throws Exception {
		SM01Vo sm01Vo = new SM01Vo();

		ModelAndView model = new ModelAndView("/sm01update");

		String userid = (String) session.getAttribute("userid");

		sm01Vo.setUserid(userid);

		sm01Vo = sm01Service.selectOneSM01Vo(sm01Vo);

		model.addObject("sm01Vo", sm01Vo);

		return model;
	}

	@RequestMapping(value = "/sm01show", method = RequestMethod.POST)
	public ModelAndView selectOneSM01Vo(@ModelAttribute("sm01Vo") SM01Vo sm01Vo) throws Exception {
		ModelAndView model = new ModelAndView("/sm01show");

		log.debug("sm01Vo : " + sm01Vo);

		sm01Vo = sm01Service.selectOneSM01Vo(sm01Vo);
		model.addObject("sM01Vo", sm01Vo);

		log.debug("sm01Vo : " + sm01Vo);

		return model;
	}

	@ResponseBody
	@RequestMapping(value = "/selectPhoto/{userid}", method = RequestMethod.GET)
	public void selectPhotoSM01Vo(@ModelAttribute SM01Vo sm01Vo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.debug("enter selectPhoto.do");
		log.debug("sm01Vo : " + sm01Vo);

		InputStream is = sm01Service.selectImage(sm01Vo, request);

		IOUtils.copy(is, response.getOutputStream());
		if (is != null) {
			try {
				is.close();
			} catch (Exception e2) {

			}
		}
		log.debug("exit selectPhoto.do");
	}
}
