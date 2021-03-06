package com.member.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.mail.service.MemberMailService;
import com.member.model.MemberService;
import com.member.model.MemberVO;

@WebServlet("/MemberServlet.do")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html; charset=utf-8");
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		if ("addMember".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
//			HttpSession session = req.getSession();
			req.setAttribute("errorMsgs", errorMsgs);
			MemberService memberSvc = new MemberService();

			try {
				String memberAccount = req.getParameter("memberAccount");
				if (memberAccount == null || memberAccount.trim().length() == 0) {
					errorMsgs.put("memberAccount", "帳號請勿空白");
				} else if (memberSvc.AccountVerify(memberAccount)) {
					errorMsgs.put("memberAccountDuplicated", "帳號已重複");
				}

				String memberPassword = req.getParameter("memberPassword");
				if (memberPassword == null || memberPassword.trim().length() == 0) {
					errorMsgs.put("memberPassword", "密碼請勿空白");
				}

				String memberEmail = req.getParameter("memberEmail");
				if (memberEmail == null || memberEmail.trim().length() == 0) {
					errorMsgs.put("memberEmail", "Email請勿空白");
				} else if (memberSvc.EmailVerify(memberEmail)) {
					errorMsgs.put("memberEmailDuplicated", "信箱已重複");
				}

				String memberName = req.getParameter("memberName");
				if (memberName == null || memberName.trim().length() == 0) {
					errorMsgs.put("memberName", "姓名請勿空白");
				}

				String memberNickName = req.getParameter("memberNickName");
				if (memberNickName == null || memberNickName.trim().length() == 0) {
					errorMsgs.put("memberNickName", "姓名請勿空白");
				}
				String memberPhone = req.getParameter("memberPhone").trim();
				if (memberPhone == null || memberPhone.isEmpty()) {
					errorMsgs.put("memberPhone", "電話請勿空白");
				}

				String memberAddress = req.getParameter("memberAddress").trim();
				if (memberAddress == null || memberAddress.isEmpty()) {
					errorMsgs.put("memberAddress", "地址請勿空白");
				}

				// default data
				java.util.Date date = new java.util.Date();
				String memberSex = "N";
				java.sql.Date memberBirth = new java.sql.Date(date.getTime());
				String memberCountry = "台灣";
				Integer memberAuth = 1;
				String memberStatus = "T";
				String memberStatusEmp = "F";
				String memberStatusComm = "F";
				String memberOnline = "Y";
				java.sql.Date memberSignupDate = new java.sql.Date(date.getTime());
				File file = new File(getServletContext().getRealPath("/") + "/frontend/image/default_user_avatar.jpg");
				FileInputStream in = new FileInputStream(file);
				byte[] memberPhoto = new byte[in.available()];
				in.read(memberPhoto);
				in.close();

				MemberVO memberVO = new MemberVO();
				memberVO.setMemberAccount(memberAccount);
				memberVO.setMemberPassword(memberPassword);
				memberVO.setMemberName(memberName);
				memberVO.setMemberNickName(memberNickName);
				memberVO.setMemberEmail(memberEmail);
				memberVO.setMemberPhoto(memberPhoto);
				memberVO.setMemberPhone(memberPhone);
				memberVO.setMemberAddress(memberAddress);
				memberVO.setMemberBirth(memberBirth);
				memberVO.setMemberSex(memberSex);
				memberVO.setMemberCountry(memberCountry);
				memberVO.setMemberSignupDate(memberSignupDate);
				memberVO.setMemberAuth(memberAuth);
				memberVO.setMemberStatus(memberStatus);
				memberVO.setMemberStatusEmp(memberStatusEmp);
				memberVO.setMemberStatusComm(memberStatusComm);
				memberVO.setMemberOnline(memberOnline);

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("memberVO", memberVO);
					RequestDispatcher failView = req.getRequestDispatcher("/frontend/member/signUp.jsp");
					failView.forward(req, res);
					return;
				}

				memberSvc.addMember(memberVO);
//				memberVO = memberSvc.getOneMemberByAccount(memberAccount);
//				req.setAttribute("memberVO", memberVO);
//				session.setAttribute("userVO", memberVO);
//				
//				String url = "/frontend/login.jsp";
//				RequestDispatcher successView = req.getRequestDispatcher(url);
//				successView.forward(req, res);
				
				String url = req.getContextPath() + "/frontend/login.jsp"; 
				res.sendRedirect(url);

			} catch (Exception e) {
				e.printStackTrace();
				RequestDispatcher failView = req.getRequestDispatcher("/frontend/error.jsp");
				failView.forward(req, res);
			}
		}

		if ("getOneUpdate".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				String memberId = req.getParameter("memberId").trim();

				MemberService memberSvc = new MemberService();
				MemberVO memberVO = memberSvc.getOneMember(memberId);

				req.setAttribute("memberVO", memberVO);

				String url = "/frontend/member/editProfile.jsp";
				RequestDispatcher sucessVeiw = req.getRequestDispatcher(url);
				sucessVeiw.forward(req, res);

			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("error", e.getMessage());
				RequestDispatcher failView = req.getRequestDispatcher("/frontend/error.jsp");
				;
				failView.forward(req, res);
			}
		}

		if ("updateMember".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);
			HttpSession session = req.getSession();
			MemberService memberSvc = new MemberService();

			try {
				String memberId = req.getParameter("memberId");
				MemberVO member = memberSvc.getOneMember(memberId);

				String memberName = req.getParameter("memberName");
				if (memberName == null || memberName.trim().length() == 0) {
					errorMsgs.put("memberName", "請勿空白");
				}

				String memberNickName = req.getParameter("memberNickName");
				if (memberNickName == null || memberNickName.trim().length() == 0) {
					errorMsgs.put("memberNickName", "請勿空白");
				}

				String memberEmail = req.getParameter("memberEmail");
				if (memberEmail == null || memberEmail.trim().length() == 0) {
					errorMsgs.put("memberEmail", "Email請勿空白");
				}

				Part part = req.getPart("memberPhoto");
				InputStream in = null;
				byte[] memberPhoto = null;
				String filename = memberSvc.getFileNameFromPart(part);
				if (filename == null || filename.isEmpty()) {
					MemberService memberService = new MemberService();
					MemberVO memberOriginPhoto = memberService.getOneMember(memberId);
					memberPhoto = memberOriginPhoto.getMemberPhoto();
				} else {
					in = part.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[8192];
					int i;
					while ((i = in.read(buffer)) != -1) {
						baos.write(buffer, 0, i);
					}
					memberPhoto = baos.toByteArray();
					baos.close();
					in.close();
				}

				String memberPhone = req.getParameter("memberPhone");
				if (memberPhone == null || memberPhone.trim().length() == 0) {
					errorMsgs.put("memberPhone", "請勿空白");
				}

				String memberAddress = req.getParameter("memberAddress");
				if (memberAddress == null || memberAddress.trim().length() == 0) {
					errorMsgs.put("memberAddress ", "請勿空白");
				}

				Date memberBirth = null;
				try {
					memberBirth = java.sql.Date.valueOf(req.getParameter("memberBirth").trim());
				} catch (IllegalArgumentException e) {
					memberBirth = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.put("memberBirth", "請輸入日期!");
				}

				MemberVO memberVO = new MemberVO();
				memberVO.setMemberId(memberId);
				memberVO.setMemberAccount(member.getMemberAccount());
				memberVO.setMemberPassword(member.getMemberPassword());
				memberVO.setMemberName(memberName);
				memberVO.setMemberNickName(memberNickName);
				memberVO.setMemberEmail(memberEmail);
				memberVO.setMemberPhoto(memberPhoto);
				memberVO.setMemberPhone(memberPhone);
				memberVO.setMemberAddress(memberAddress);
				memberVO.setMemberBirth(memberBirth);
				memberVO.setMemberSex(member.getMemberSex());
				memberVO.setMemberCountry(member.getMemberCountry());
				memberVO.setMemberSignupDate(member.getMemberSignupDate());
				memberVO.setMemberAuth(member.getMemberAuth());
				memberVO.setMemberStatus(member.getMemberStatus());
				memberVO.setMemberStatusEmp(member.getMemberStatusEmp());
				memberVO.setMemberStatusComm(member.getMemberStatusComm());
				memberVO.setMemberOnline(member.getMemberOnline());

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("memberVO", memberVO);
					RequestDispatcher failView = req.getRequestDispatcher("/frontend/member/editProfile.jsp");
					failView.forward(req, res);
					return;
				}

				memberVO = memberSvc.updateMember(memberVO);
				req.setAttribute("memberVO", memberVO);
				session.setAttribute("userVO", memberVO);

				String url = "/frontend/member/memberSetting.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("error", e.getMessage());
				RequestDispatcher failView = req.getRequestDispatcher("/frontend/error.jsp");
				failView.forward(req, res);
			}
		}

		if ("changePassword".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);
			HttpSession session = req.getSession();
			MemberService memberSvc = new MemberService();

			try {
				String memberId = req.getParameter("memberId");
				MemberVO member = memberSvc.getOneMember(memberId);

				String oldPassword = req.getParameter("oldPassword");
				if (oldPassword == null || oldPassword.trim().length() == 0) {
					errorMsgs.put("oldPassword", "舊碼請勿空白");
				}

				if (!memberSvc.OldPasswordVerify(oldPassword, memberId)) {
					errorMsgs.put("oldPasswordVerify", "舊密碼錯誤");
				}

				String memberPassword = req.getParameter("memberPassword");
				if (memberPassword == null || memberPassword.trim().length() == 0) {
					errorMsgs.put("memberPassword", "新密碼請勿空白");
				}

				String confirmPassword = req.getParameter("confirmPassword");
				if (confirmPassword == null || confirmPassword.trim().length() == 0) {
					errorMsgs.put("confirmPassword", "確認密碼請勿空白");
				}

				if (!confirmPassword.equals(memberPassword)) {
					errorMsgs.put("confirmPassword", "確認密碼不符合");
				}

				MemberVO memberVO = new MemberVO();
				memberVO.setMemberId(memberId);
				memberVO.setMemberAccount(member.getMemberAccount());
				memberVO.setMemberPassword(memberPassword);
				memberVO.setMemberName(member.getMemberName());
				memberVO.setMemberNickName(member.getMemberNickName());
				memberVO.setMemberEmail(member.getMemberEmail());
				memberVO.setMemberPhoto(member.getMemberPhoto());
				memberVO.setMemberPhone(member.getMemberPhone());
				memberVO.setMemberAddress(member.getMemberAddress());
				memberVO.setMemberBirth(member.getMemberBirth());
				memberVO.setMemberSex(member.getMemberSex());
				memberVO.setMemberCountry(member.getMemberCountry());
				memberVO.setMemberSignupDate(member.getMemberSignupDate());
				memberVO.setMemberAuth(member.getMemberAuth());
				memberVO.setMemberStatus(member.getMemberStatus());
				memberVO.setMemberStatusEmp(member.getMemberStatusEmp());
				memberVO.setMemberStatusComm(member.getMemberStatusComm());
				memberVO.setMemberOnline(member.getMemberOnline());

				if (!errorMsgs.isEmpty()) {
					req.setAttribute("memberVO", memberVO);
					RequestDispatcher failView = req.getRequestDispatcher("/frontend/member/changePassword.jsp");
					failView.forward(req, res);
					return;
				}

				memberVO = memberSvc.updateMember(memberVO);
				req.setAttribute("memberVO", memberVO);
				session.setAttribute("userVO", memberVO);

				String url = "/frontend/member/memberSetting.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);
			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("error", e.getMessage());
				RequestDispatcher failView = req.getRequestDispatcher("/frontend/error.jsp");
				failView.forward(req, res);
			}
		}

		if ("forgotPassword".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			Map<String, String> successMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);
			req.setAttribute("successMsgs", successMsgs);
			MemberService memberSvc = new MemberService();
			MemberMailService memMailSvc = new MemberMailService();

			try {
				String email = req.getParameter("memberEmail");
				
				if (email == null || email.trim().length() == 0) {
					errorMsgs.put("memberEmail", "請勿空白");
				}
				
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failView = req.getRequestDispatcher("/frontend/member/forgotPassword.jsp");
					failView.forward(req, res);
					return;
				}

				MemberVO memberVO = memberSvc.getOneMemberByEmail(email);

				if (memberVO == null) {
					errorMsgs.put("memberEmail", "查無資料，請確認信箱帳號");
				}

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failView = req.getRequestDispatcher("/frontend/member/forgotPassword.jsp");
					failView.forward(req, res);
					return;
				}

				String password = memberSvc.randomPassword();
				System.out.println("test password = " + password);
				memberSvc.UpdateMemberPassword(password, email);
				memMailSvc.sendMail(email, "您的新密碼", "Hello " + memberVO.getMemberName() + " 請謹記此密碼: " + password + "");
				successMsgs.put("successMsgs","成功寄出，請至信箱確認");
				
				String url = "/frontend/member/forgotPassword.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("error", e.getMessage());
				RequestDispatcher failView = req.getRequestDispatcher("/frontend/error.jsp");
				failView.forward(req, res);
			}
		}

		if ("deleteMember".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				String memberId = req.getParameter("memberId").trim();

				MemberService memberSvc = new MemberService();
				memberSvc.deleteMember(memberId);

				String url = "/member/getAllMember.jsp";
				RequestDispatcher sucessVeiw = req.getRequestDispatcher(url);
				sucessVeiw.forward(req, res);

			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("error", e.getMessage());
				RequestDispatcher exceptionView = req.getRequestDispatcher("/frontend/error.jsp");
				exceptionView.forward(req, res);
			}
		}

		if ("getOneMember".equals(action)) {
			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				String memberId = req.getParameter("memberId").trim();
				if (memberId == null || (memberId.trim()).length() == 0) {
					errorMsgs.put("memberId", "請勿空白");
				}

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failView = req.getRequestDispatcher("/member/MemberHome.jsp");
					failView.forward(req, res);
					return;
				}

				MemberService memberSvc = new MemberService();
				MemberVO memberVO = new MemberVO();
				memberVO = memberSvc.getOneMember(memberId);
				if (memberVO == null) {
					errorMsgs.put("memberId", "查無資料");
				}
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failView = req.getRequestDispatcher("/member/MemberHome.jsp");
					failView.forward(req, res);
					return;
				}

				req.setAttribute("memberVO", memberVO);

				String url = "/frontend/member/aboutMe.jsp";
				RequestDispatcher sucessVeiw = req.getRequestDispatcher(url);
				sucessVeiw.forward(req, res);

			} catch (Exception e) {
				e.printStackTrace();
				errorMsgs.put("error", e.getMessage());
				RequestDispatcher exceptionView = req.getRequestDispatcher("/frontend/error.jsp");
				exceptionView.forward(req, res);
			}
		}

		if ("selectAllMemberIdName".equals(action)) {
			try {
				MemberService memberSvc = new MemberService();

				Map<String, String> map = memberSvc.selectAllMemberIdName();
				JSONObject jObj = new JSONObject(map);
				res.getWriter().write(new Gson().toJson(jObj));

			} catch (Exception e) {
				e.printStackTrace();
				RequestDispatcher exceptionView = req.getRequestDispatcher("/backend/error.jsp");
				exceptionView.forward(req, res);
			}
		}
		if ("selectAllMemberIdNameR".equals(action)) {
			try {
				MemberService memberSvc = new MemberService();

				Map<String, String> map = memberSvc.selectAllEmpIdNameR();
				JSONObject jObj = new JSONObject(map);
				res.getWriter().write(new Gson().toJson(jObj));
			} catch (Exception e) {
				e.printStackTrace();
				RequestDispatcher exceptionView = req.getRequestDispatcher("/backend/error.jsp");
				exceptionView.forward(req, res);
			}
		}
	}
}