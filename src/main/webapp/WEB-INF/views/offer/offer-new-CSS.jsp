<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Optical Illusion
  Date: 02.11.2015
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Страница объявления</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/resources/css/bootstrap.css">
    <link rel="stylesheet" href="/resources/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/resources/css/jquery.bxslider.css">
    <link rel="stylesheet" href="/resources/css/main.css">
    <link rel="stylesheet" href="/resources/css/font-awesome.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="/resources/css/custom-style.css">
</head>
<body>

<!--[if lt IE 8]>
<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> to improve your experience.</p>
<![endif]-->

<jsp:include page="/WEB-INF/templates/common-header.jsp"/>

<jsp:include page="/WEB-INF/templates/logo-section.jsp"/>

<jsp:include page="/WEB-INF/templates/search-bar.jsp"/>

<jsp:include page="/WEB-INF/templates/services-menu.jsp"/>


<div class="container2">
    <ul class="page-navigation" id="breadcrumbs">
    </ul>
    <ul class="page-navigation" id="offer-cities">
    </ul>
    <h1 class="title-h1-blue offer-title"></h1>
    <div class="offer-info-wrap">
        <div class="offer-info-box">
            <div class="reservation">
                <div class="col-xs-6">ЗАБРОНИРОВАТЬ</div>
                <div class="col-xs-6 text-right"><span class="offer-price"></span><span class="currency"></span> <i class="fa fa-lock"></i></div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
                <div class="col-xs-5">
                    <a href="#" class="btn btn-block btn-success">Написать автору</a>

                    <div class="clearfix"></div>
                    <div class="contact phone-numbers">
                        <span class="show-number">Показать номера телефонов</span>

                    </div>
                    <div class="clearfix"></div>
                    <div class="contact"><span class="offer-skype"></span> <i class="fa fa-skype"></i></div>
                </div>
                <div class="col-xs-7">
                    Автор: Вася Пупкин
                    <div class="clearfix"></div>
                    <span class="author-rating">20</span>

                    <div class="clearfix"></div>
                    <a href="#">Все обьявления автора</a>
                </div>
            </div>
            <div class="clearfix"></div>
            <section class="offer-map">
            </section>
            <section class="offer-video">
            </section>
        </div>
        <div class="offer-info-slider"><img src="images/slider.jpg" width="450" alt=""></div>
    </div>


    <h2 class="title-h2-blue">Описание</h2>

    <div class="descr-text-box">
        <p class="offer-description"></p>
    </div>

    <h2 class="title-h2-blue text-center">САМЫЕ ПОСЛЕДНИЕ ДОБАВЛЕНИЯ</h2>

    <%--<ul class="notice-box">--%>
        <%--<li>--%>
            <%--<a href="#" class="image"><i class="fa fa-star"></i><img src="images/pic.jpg" alt="">Заголовок</a>--%>
            <%--<a href="#" class="btn btn-xs btn-warning">1 000 грн.</a>--%>
            <%--<span>Просмотров: 222</span>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="#" class="image"><i class="fa fa-star"></i><img src="images/pic.jpg" alt="">Заголовок</a>--%>
            <%--<a href="#" class="btn btn-xs btn-warning">1 000 грн.</a>--%>
            <%--<span>Просмотров: 222</span>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="#" class="image"><i class="fa fa-star"></i><img src="images/pic.jpg" alt="">Заголовок</a>--%>
            <%--<a href="#" class="btn btn-xs btn-warning">1 000 грн.</a>--%>
            <%--<span>Просмотров: 222</span>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="#" class="image"><i class="fa fa-star"></i><img src="images/pic.jpg" alt="">Заголовок</a>--%>
            <%--<a href="#" class="btn btn-xs btn-warning">1 000 грн.</a>--%>
            <%--<span>Просмотров: 222</span>--%>
        <%--</li>--%>
        <%--<li>--%>
            <%--<a href="#" class="image"><i class="fa fa-star"></i><img src="images/pic.jpg" alt="">Заголовок</a>--%>
            <%--<a href="#" class="btn btn-xs btn-warning">1 000 грн.</a>--%>
            <%--<span>Просмотров: 222</span>--%>
        <%--</li>--%>
    <%--</ul>--%>


    <div class="container2">


        <ul class="notice-box">
        </ul>
        <!-- li pattern for clone -->
        <li id="li-offer-basic" style="display:none">
            <a href="#" class="image"><img src="/resources/images/no_photo.jpg" alt="">

                <p>Заголовок обьявления</p></a>
            <a href="#" class="btn btn-xs btn-warning">1 000 грн.</a>
            <span>Просмотров: 222</span>
        </li>
        <!-- li pattern for clone -->
        <div class="contentContainer" style="margin-top: 5px">
            <img class="projAndInvestCaretDown" id="btn-offers-more" src="/resources/images/caret.png" alt="caret">
        </div>

        <div class="feedFooter"></div>
    </div>

</div>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.js"></script>
<script>window.jQuery || document.write('<script src="/resources/js/vendor/jquery-1.11.2.js"><\/script>')</script>
<script src="/resources/js/vendor/bootstrap.js"></script>
<script src="/resources/js/jquery.bxslider.js"></script>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/jquery.easytabs/3.2.0/jquery.easytabs.min.js"></script>
<script src="/resources/libs/jquery-ui-1.11.4/jquery-ui.min.js"></script>
<sec:authorize access="isAuthenticated()">
    <jsp:include page="/WEB-INF/templates/support-questions.jsp"/>
</sec:authorize>
<jsp:include page="/WEB-INF/templates/footer.jsp"/>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.js"></script>
<script>window.jQuery || document.write('<script src="/resources/js/vendor/jquery-1.11.2.js"><\/script>')</script>
<script src="/resources/js/vendor/bootstrap.js"></script>
<script src="/resources/js/jquery.bxslider.js"></script>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/jquery.easytabs/3.2.0/jquery.easytabs.min.js"></script>
<script src="/resources/libs/jquery-ui-1.11.4/jquery-ui.min.js"></script>
<sec:authorize var="loggedIn" access="isAuthenticated()"/>
<c:choose>
    <c:when test="${loggedIn}">
        <script src="/resources/js/autorizedHeader.js"></script>
    </c:when>
    <c:otherwise>
        <script src="/resources/js/anonymHeader.js"></script>
    </c:otherwise>
</c:choose>
<script src="/resources/js/main.js"></script>
<script src="/resources/js/logo-section.js"></script>
<script src="/resources/js/search-bar.js"></script>



<script>
    var offerId = "${offerId}";
</script>
<script src="/resources/js/offer.js"></script>
</body>
</html>