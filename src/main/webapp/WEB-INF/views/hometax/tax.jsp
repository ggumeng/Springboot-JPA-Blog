<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<button id="btn-select" class="btn btn-primary">조회</button>
	<br> <br> <br>
	<div class="card">
		<div class="card-header">사업자번호 리스트</div>
		<ul id="reply-box" class="list-group">
			<c:forEach var="no" items="${nos}">
				<li id="test" class="list-group-item d-flex justify-content-between">
					<div class="number">${no}</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>

<script src="/js/tax.js"></script>
<%@ include file="../layout/footer.jsp"%>

