<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.orderMaster.model.*"%>
<%@ page import="com.orderDetail.model.*"%>
<%@ page import="com.spaceDetail.model.*"%>
<%@ page import="com.space.model.*"%>
<%@ page import="com.emp.model.*"%>

<%
	
	List<OrderDetailVO> list = (List<OrderDetailVO>)request.getAttribute("selectOneOrderDetailByMaster");
	pageContext.setAttribute("list",list);
	
	String orderMasterId = (String)request.getAttribute("orderMasterId");
	
%>

<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="Ansonika">
  <title>訂單明細</title>
	
  <!-- Favicons-->
  <link rel="shortcut icon" href="img/favicon.ico" type="<%=request.getContextPath()%>/backend/image/x-icon">
  <link rel="apple-touch-icon" type="image/x-icon" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-57x57-precomposed.png">
  <link rel="apple-touch-icon" type="image/x-icon" sizes="72x72" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-72x72-precomposed.png">
  <link rel="apple-touch-icon" type="image/x-icon" sizes="114x114" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-114x114-precomposed.png">
  <link rel="apple-touch-icon" type="image/x-icon" sizes="144x144" href="<%=request.getContextPath()%>/backend/img/apple-touch-icon-144x144-precomposed.png">

  <!-- GOOGLE WEB FONT -->
  <link href="<%=request.getContextPath()%>/backend/https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800" rel="stylesheet">
	
  <!-- Bootstrap core CSS-->
  <link href="<%=request.getContextPath()%>/backend/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <!-- Main styles -->
  <link href="<%=request.getContextPath()%>/backend/css/admin.css" rel="stylesheet">
  <!-- Icon fonts-->
  <link href="<%=request.getContextPath()%>/backend/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
  <!-- Plugin styles -->
  <link href="<%=request.getContextPath()%>/backend/vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">
  <!-- Your custom styles -->
  <link href="<%=request.getContextPath()%>/backend/css/custom.css" rel="stylesheet">
	
</head>


<body class="fixed-nav sticky-footer" id="page-top">

<%@ include file="/backend/backendHF.jsp" %> 
  
 
 <!-- /Navigation-->
  <div class="content-wrapper">
    <div class="container-fluid">
      <!-- Breadcrumbs-->
      <ol class="breadcrumb">
        <li class="breadcrumb-item active">訂單編號:<%= orderMasterId%></li>
      </ol>
      
      
		<!-- Example DataTables Card-->
      <div class="card mb-3">
        <div class="card-header">
          <i class="fa fa-table"></i>  訂單明細</div>
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
				<tr>
					<th>明細</th>
					<th>租借開始時間</th>
					<th>租借結束時間</th>
					<th>時段金額</th>
					<th>場地</th>
				</tr>
              </thead>
              <tfoot>
				<tr>
					<th>明細</th>
					<th>租借開始時間</th>
					<th>租借結束時間</th>
					<th>時段金額</th>
					<th>場地</th>
				</tr>
              </tfoot>

<c:forEach var="orderDetailVO" items="${list}" begin="0" end="<%=list.size()%>" varStatus="count">
		<jsp:useBean id="orderDetailServ" scope="page" class="com.orderDetail.model.OrderDetailServiceB" />
		<jsp:useBean id="spaceDetailServ" scope="page" class="com.spaceDetail.model.SpaceDetailServiceB" />
		<jsp:useBean id="spaceServ" scope="page" class="com.space.model.SpaceServiceB" />
		<%
			String orderDetailId = ((OrderDetailVO)pageContext.getAttribute("orderDetailVO")).getOrderDetailId();
			
			OrderDetailVO selectOneOrderDetail = new OrderDetailVO();
			selectOneOrderDetail = orderDetailServ.selectOneOrderDetail(orderDetailId);
			
			String spaceDetailId = selectOneOrderDetail.getSpaceDetailId();
			SpaceDetailVO SpaceDetail = spaceDetailServ.selectOneSpaceDetail(spaceDetailId);
			
			String spaceId = SpaceDetail.getSpaceId();
			SpaceVO spaceVO = spaceServ.selectOneSpace(spaceId);
			
			String spaceName = spaceVO.getSpaceName();
		%>
	<tr>
		<td>${count.count}</td>
		<td>${orderDetailVO.rentStartTime}</td>
		<td>${orderDetailVO.rentEndTime}</td>
		<td><%=SpaceDetail.getSpaceDetailCharge()%></td>
		<td>
			<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/SpaceServlet">[<%=spaceId%>]<%=spaceName%>
				<button type="submit" class="btn btn-link">
					<svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-box-arrow-in-right" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
					  <path fill-rule="evenodd" d="M6 3.5a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-2a.5.5 0 0 0-1 0v2A1.5 1.5 0 0 0 6.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-8A1.5 1.5 0 0 0 5 3.5v2a.5.5 0 0 0 1 0v-2z"/>
					  <path fill-rule="evenodd" d="M11.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H1.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"/>
					</svg>
        		</button>
				<input type="hidden" name="spaceId"  value="<%=spaceId%>">
				<input type="hidden" name="action"	value="backend_SelectOneSpace">
			</FORM>
		</td>
	</tr>
</c:forEach>
 
 
 
              </tbody>
            </table>
          </div>
        </div>
        <div class="card-footer small text-muted">Updated yesterday at 11:59 PM</div>
      </div>
	  <!-- /tables-->
	  </div>
	  <!-- /container-fluid-->
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
    <script src="<%=request.getContextPath()%>/backend/js/admin-datatables.js"></script>
</body>

</html>