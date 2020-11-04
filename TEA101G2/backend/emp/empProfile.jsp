<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.emp.model.*"%>

<%
	EmpVO empVO = (EmpVO)request.getAttribute("selectOneEmp");
	Base64.Encoder encode = Base64.getEncoder();
	
	EmpService pageEmpServ = new EmpService();
	String empAccount = empVO.getEmpAccount();
	empVO = pageEmpServ.selectAllEmpByAccount(empAccount);
	String empId = empVO.getEmpId();
	
%>

<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="Ansonika">
  <title>員工資料</title>
	
  <!-- Favicons-->
  <link rel="shortcut icon" href="<%=request.getContextPath()%>/backend/img/favicon.ico" type="image/x-icon">
  <link rel="apple-touch-icon" type="image/x-icon" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-57x57-precomposed.png">
  <link rel="apple-touch-icon" type="image/x-icon" sizes="72x72" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-72x72-precomposed.png">
  <link rel="apple-touch-icon" type="image/x-icon" sizes="114x114" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-114x114-precomposed.png">
  <link rel="apple-touch-icon" type="image/x-icon" sizes="144x144" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-144x144-precomposed.png">

  <!-- GOOGLE WEB FONT -->
  <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800" rel="stylesheet">
	
  <!-- Bootstrap core CSS-->
  <link href="<%=request.getContextPath()%>/backend/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Main styles -->
  <link href="<%=request.getContextPath()%>/backend/css/admin.css" rel="stylesheet">
  <!-- Icon fonts-->
  <link href="<%=request.getContextPath()%>/backend/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Plugin styles -->
  <link href="<%=request.getContextPath()%>/backend/vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">
  <link href="<%=request.getContextPath()%>/backend/vendor/dropzone.css" rel="stylesheet">
  <!-- Your custom styles -->
  <link href="<%=request.getContextPath()%>/backend/css/custom.css" rel="stylesheet">
  
 <style>
    img.preview {
      width: 100px;
    }
</style>
	
</head>


<body class="fixed-nav sticky-footer" id="page-top">

<%@ include file="/backend/backendHF.jsp" %>  

  <!-- /Navigation-->
  <div class="content-wrapper">
    <div class="container-fluid">
      <!-- Breadcrumbs-->
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <a href="<%=request.getContextPath()%>/backend/index.jsp">首頁</a>
        </li>
        <li class="breadcrumb-item">
          <a href="<%=request.getContextPath()%>/backend/emp/selectEmp.jsp">搜尋員工</a>
        </li>
        <li class="breadcrumb-item active">[<%=empId%>]<%= empVO.getEmpName()%></li>
      </ol>
		<div class="box_general padding_bottom">
			<div class="header_box version_2">
				<h2><i class="fa fa-user"></i><%=empId%> / <%= empVO.getEmpName()%></h2>
			</div>
			
			<img src="<%=(empVO.getEmpPhoto() == null)? application.getRealPath("/backend/img/BlobTest3") : "data:image/png;base64," + encode.encodeToString(empVO.getEmpPhoto())%>" class="perview"/>


 			<div class="row">
				<div class="col-md-8 add_top_30">
					<div class="row">

						<div class="col-md-6">
							<div class="form-group">
								<label>員工連絡電話</label>
								<input type="text" class="form-control" name="empPhone" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpPhone()%>"/>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label>員工聯絡地址</label>
								<input type="text" class="form-control" name="empAddress" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpAddress()%>"/>
								
							</div>
						</div>
						
						<div class="col-md-12">
							<div class="form-group">
								<label>員工Email</label>
								<input type="text" class="form-control" name="empEmail" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpEmail()%>"/>
							</div>
						</div>

					

						<div class="col-md-6">
							<div class="form-group">
								<label>員工生日</label>
								<input type="text" class="form-control" name="empBirth" id="empBirth" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpBirth()%>"/>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label>員工性別</label>
								<input type="text" class="form-control" name="empSex" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpSex()%>"/>
								
							</div>
						</div>
	
					

						<div class="col-md-6">
							<div class="form-group">
								<label>員工國籍</label>
								<input type="text" class="form-control" name="empCountry" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpCountry()%>"/>
								
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label>員工到職日</label>
								<input type="text" class="form-control" name="empHireDate" id="empHireDate" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpHireDate()%>"/>
							</div>
						</div>

					

						<div class="col-md-4">
							<div class="form-group">
								<label>員工職稱</label> 
								<input type="text" class="form-control" name="empJob" readonly
									value="<%= (empVO == null)? "" : empVO.getEmpJob()%>"/>
								
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>員工權限</label>
								<input type="text" class="form-control" name="empAuth" readonly
									value="<%= (empVO == null)? "" : (empVO.getEmpAuth() == 1)? "員工": "主管"%>"/>

							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>員工在職狀態</label>
								<input type="text" class="form-control" name="empStatus" readonly
									value="<%= (empVO == null)? "" : (empVO.getEmpStatus().equals("O"))? "在職": (empVO.getEmpStatus().equals("P"))? "停職" : "離職"%>"/>
								
							</div>
						</div>
					</div>
					
				</div>
			</div>
				
			
		</div>
		
	  </div>
	  
   	</div>
   	
   	
    
    <!-- Bootstrap core JavaScript-->
    <script src="<%=request.getContextPath()%>/backend/vendor/jquery/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/backend/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <!-- Core plugin JavaScript-->
    <script src="<%=request.getContextPath()%>/backend/vendor/jquery-easing/jquery.easing.min.js"></script>
    <!-- Page level plugin JavaScript-->
    <script src="<%=request.getContextPath()%>/backend/vendor/chart.js/Chart.min.js"></script>
    <script src="<%=request.getContextPath()%>/backend/vendor/datatables/jquery.dataTables.js"></script>
    <script src="<%=request.getContextPath()%>/backend/vendor/datatables/dataTables.bootstrap4.js"></script>
	<script src="<%=request.getContextPath()%>/backend/vendor/jquery.selectbox-0.2.js"></script>
	<script src="<%=request.getContextPath()%>/backend/vendor/retina-replace.min.js"></script>
	<script src="<%=request.getContextPath()%>/backend/vendor/jquery.magnific-popup.min.js"></script>
    <!-- Custom scripts for all pages-->
    <script src="<%=request.getContextPath()%>/backend/js/admin.js"></script>
	<!-- Custom scripts for this page-->
	<script src="<%=request.getContextPath()%>/backend/vendor/dropzone.min.js"></script>
	
</body>

</html>