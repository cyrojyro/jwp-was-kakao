package service;

import db.DataBase;
import domain.HttpRequest;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.HttpRequstParser;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberServiceTest {
	private MemberService underTest = new MemberService();
	private HttpRequstParser httpRequstParser;
	private HttpRequest httpRequest;

	@Test
	@DisplayName("회원가입요청인지 확인한다.")
	public void isJoinMemberTest() {
		joinMemberRequestData();
		assertTrue(underTest.isMemberJoinRequst(httpRequest));
	}

	@Test
	@DisplayName("회원가입시 유저 정보를 저장한다")
	public void joinMemberTest() {
		joinMemberRequestData();
		underTest.joinMember(httpRequest.getRequestParam());
		User user = DataBase.findUserById("adeldel");
		assertThat(user.getName()).isEqualTo("adeldel");
		assertThat(user.getPassword()).isEqualTo("password");
	}

	@Test
	@DisplayName("로그인 요청인지 확인한다.")
	public void memberLoginRequestTest() {
		loginRequstData("password");
		assertTrue(underTest.memberLoginRequest(httpRequest));
	}

	@Test
	@DisplayName("로그인시 유저정보를 확인한다.")
	public void loginRequestTest() {
		joinMemberRequestData();
		underTest.joinMember(httpRequest.getRequestParam());

		loginRequstData("password");
		assertTrue(underTest.memberLogin(httpRequest.getRequestParam()));

		loginRequstData("wrong_password");
		assertFalse(underTest.memberLogin(httpRequest.getRequestParam()));
	}

	@Test
	@DisplayName("회원목록 호출인지  확인한다.")
	public void isMemberListRequestTest() {
		setListRequestData();
		assertTrue(underTest.isMemberListRequest(httpRequest));
	}

	@Test
	@DisplayName("모든 회원정보를 가져온다.")
	public void getAllMembersTest() {
		//회원가입
		joinMemberRequestData();
		underTest.joinMember(httpRequest.getRequestParam());
		//모든회원 조회
		List<User> members = underTest.getAllMembers();
		assertThat(members).hasSize(1);
		assertThat(members.get(0).getUserId()).isEqualTo("adeldel");
	}

	private void joinMemberRequestData() {
		Reader reader = new StringReader("POST /user/create HTTP/1.1\n" +
				"Host: localhost:8080\n" +
				"Connection: keep-alive\n" +
				"Content-Length: 67\n" +
				"Content-Type: application/x-www-form-urlencoded\n" +
				"Accept: */*\n\n" +
				"userId=adeldel&password=password&name=adeldel&email=adel%40daum.net");

		makeHttpRequest(reader);
	}

	private void loginRequstData(String password) {
		Reader reader = new StringReader("POST /user/login HTTP/1.1\n" +
				"Host: localhost:8080\n" +
				"Connection: keep-alive\n" +
				"Content-Length: 32\n" +
				"Content-Type: application/x-www-form-urlencoded\n" +
				"Accept: */*\n\n" +
				"userId=adeldel&password=" + password);

		makeHttpRequest(reader);
	}

	private void setListRequestData() {
		Reader reader = new StringReader("GET /user/list HTTP/1.1\n" +
				"Host: localhost:8080\n" +
				"Connection: keep-alive\n" +
				"Content-Type: application/x-www-form-urlencoded\n" +
				"Accept: */*\n\n");

		makeHttpRequest(reader);
	}

	private void makeHttpRequest(Reader reader) {
		httpRequstParser = new HttpRequstParser(new BufferedReader(reader));
		httpRequest = httpRequstParser.requestParse();

	}
}
