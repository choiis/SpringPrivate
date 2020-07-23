package com.singer.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyVo extends SuperVo {

	private static final long serialVersionUID = 1940077307178232725L;
	protected int seq;
	protected int seq01;
	protected int parents;
	protected String text;
	protected String userid;
	protected String regdate;
	protected int good;
	protected int reply;

}
