<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ru">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Арендатор | Панель управления</title>
    <meta charset='utf-8' />

    <!-- Links -->
    <jsp:include page="/WEB-INF/templates/admin-panel/admin-top-links.jsp"/>
    <!-- Links -->

    <script>
        var gupEvents = [];
        var offerResult;

        $(document).ready(function() {

            function formattedDate(date) {
                var d = new Date(date || Date.now()),
                        month = '' + (d.getMonth() + 1),
                        day = '' + d.getDate(),
                        year = d.getFullYear();
                if (month.length < 2) month = '0' + month;
                if (day.length < 2) day = '0' + day;

                return [year, month, day].join('-');
            }

            function parseJsonWeekday(monthOfPrices) {
                var gupEvents2 = [];

                var   START_DATE = monthOfPrices.weekday.days[0].split(".");
                var     END_DATE = monthOfPrices.weekday.days[1].split(".");
                var F_START_DATE = new Date(START_DATE[2], START_DATE[1] - 1, START_DATE[0]);
                var   F_END_DATE = new Date(END_DATE[2], END_DATE[1] - 1, END_DATE[0]);

                var D_START = new Date(F_START_DATE);
                var   D_END = new Date(F_END_DATE);

                var   WEEKDAY = -1;
                var isWEEKDAY = true;
                while(D_START.getDate() < D_END.getDate()){
                    if( D_START.getDay()%6 && D_START.getDay()%7 ){
                        if( isWEEKDAY ){
                            WEEKDAY++;
//				            console.log( '(1) ' + D_START ) //...
                            var tmpDate2 = D_START.setDate(D_START.getDate());
                            TMP_START2 = new Date(tmpDate2);
                            gupEvents2[WEEKDAY] = { title:monthOfPrices.weekday.price, start:formattedDate(TMP_START2), color:'#2980b9' };
                            isWEEKDAY = false;
                        }
                    } else {
                        if( !isWEEKDAY ){
                            var tmpDate = D_START.setDate(D_START.getDate() - 1);
                            TMP_START = new Date(tmpDate);
//				            console.log( '(2) ' + TMP_START ) //...
//				            console.log( ) //...
                            //gupEvents2[WEEKDAY].end = formattedDate(TMP_START); //...
                            tmpDate2 = TMP_START.setDate(TMP_START.getDate()+1); //...
                            TMP_START2 = new Date(tmpDate2); //...
                            gupEvents2[WEEKDAY].end = formattedDate(TMP_START2); //...

                            isWEEKDAY = true;
                        }
                    }
                    var newDate = D_START.setDate(D_START.getDate() + 1);
                    D_START = new Date(newDate);
                }

                if( !isWEEKDAY ){
                    if( D_START.getDay()%6 && D_START.getDay()%7 ){
                        tmpDate = D_START.setDate(D_START.getDate());
                        TMP_START = new Date(tmpDate);
//				        console.log( '(3) ' + TMP_START ) //...
                        //gupEvents2[WEEKDAY].end = formattedDate(TMP_START); //...
                        tmpDate2 = TMP_START.setDate(TMP_START.getDate()+1); //...
                        TMP_START2 = new Date(tmpDate2); //...
                        gupEvents2[WEEKDAY].end = formattedDate(TMP_START2); //...
                    } else {
                        tmpDate = D_START.setDate(D_START.getDate() - 1);
                        TMP_START = new Date(tmpDate);
//				        console.log( '(4) ' + TMP_START ) //...
                        //gupEvents2[WEEKDAY].end = formattedDate(TMP_START); //...
                        tmpDate2 = TMP_START.setDate(TMP_START.getDate()+1); //...
                        TMP_START2 = new Date(tmpDate2); //...
                        gupEvents2[WEEKDAY].end = formattedDate(TMP_START2); //...
                    }
                }
                if( isWEEKDAY ){
                    if( D_START.getDay()%6 && D_START.getDay()%7 ){
                        WEEKDAY++;
                        tmpDate = D_START.setDate(D_START.getDate());
                        TMP_START = new Date(tmpDate);
//				        console.log( '(5) ' + TMP_START ) //...
                        gupEvents2[WEEKDAY] = { title:monthOfPrices.weekday.price, start:formattedDate(TMP_START), color:'#2980b9' };
                    }
                }

//				//console.log( 'WEEKDAY = ' + WEEKDAY )
//				console.log( Object.keys(gupEvents2).length )
//				console.log( gupEvents2 )
                return gupEvents2
            }

            function parseJsonWeekend(monthOfPrices) {
                var gupEvents2 = [];

                var   START_DATE = monthOfPrices.weekend.days[0].split(".");
                var     END_DATE = monthOfPrices.weekend.days[1].split(".");
                var F_START_DATE = new Date(START_DATE[2], START_DATE[1] - 1, START_DATE[0]);
                var   F_END_DATE = new Date(END_DATE[2], END_DATE[1] - 1, END_DATE[0]);

                var D_START = new Date(F_START_DATE);
                var   D_END = new Date(F_END_DATE);

                var   WEEKEND = -1;
                var isWEEKEND = true;
                while(D_START.getDate() < D_END.getDate()){
                    if( !(D_START.getDay()%6 && D_START.getDay()%7) ){
                        if( isWEEKEND ){
                            WEEKEND++;
//				        	console.log( '(1) ' + D_START ) //...
                            var tmpDate2 = D_START.setDate(D_START.getDate());
                            TMP_START2 = new Date(tmpDate2);
                            gupEvents2[WEEKEND] = { title:monthOfPrices.weekend.price, start:formattedDate(TMP_START2), color:'#2980b9' };
                            isWEEKEND = false;
                        }
                    } else {
                        if( !isWEEKEND ){
                            var tmpDate = D_START.setDate(D_START.getDate());
                            TMP_START = new Date(tmpDate);
//				            console.log( '(2) ' + TMP_START ) //...
//				            console.log( ) //...
                            gupEvents2[WEEKEND].end = formattedDate(TMP_START);
                            isWEEKEND = true;
                        }
                    }
                    var newDate = D_START.setDate(D_START.getDate() + 1);
                    D_START = new Date(newDate);
                }

                if( !isWEEKEND ){
                    if( !(D_START.getDay()%6 && D_START.getDay()%7) ){
                        tmpDate = D_START.setDate(D_START.getDate() + 1);
                        TMP_START = new Date(tmpDate);
//				    	console.log( '(3) ' + TMP_START ) //...
                        gupEvents2[WEEKEND].end = formattedDate(TMP_START);
                    } else {
                        tmpDate = D_START.setDate(D_START.getDate());
                        TMP_START = new Date(tmpDate);
//				     	console.log( '(4) ' + TMP_START ) //...
                        gupEvents2[WEEKEND].end = formattedDate(TMP_START);
                    }
                }
                if( isWEEKEND ){
                    if( !(D_START.getDay()%6 && D_START.getDay()%7) ){
                        WEEKEND++;
                        tmpDate = D_START.setDate(D_START.getDate() + 1);
                        TMP_START = new Date(tmpDate);
//				    	console.log( '(5) ' + TMP_START ) //...
                        gupEvents2[WEEKEND] = { title:monthOfPrices.weekend.price, start:formattedDate(TMP_START), color:'#2980b9' };
                    }
                }

//				//console.log( 'WEEKEND = ' + WEEKEND )
//				console.log( Object.keys(gupEvents2).length )
//				console.log( gupEvents2 )
                return gupEvents2
            }

            function parseJsonSpecialdays(monthOfPrices) {
                var gupEvents2 = [];

                var SPECIALDAY = -1;
                while(SPECIALDAY < (Object.keys(monthOfPrices.specialdays).length - 1)){
                    SPECIALDAY++;

                    var   START_DATE = monthOfPrices.specialdays[SPECIALDAY].days[0].split(".");
                    var F_START_DATE = new Date(START_DATE[2], START_DATE[1] - 1, START_DATE[0]);

                    var      D_START = new Date(F_START_DATE);

//	  			  console.log( '(1) ' + D_START ) //...
                    var tmpDate2 = D_START.setDate(D_START.getDate());
                    TMP_START2 = new Date(tmpDate2);
                    gupEvents2[SPECIALDAY] = { title:monthOfPrices.specialdays[SPECIALDAY].price, start:formattedDate(TMP_START2), color:'#2980b9' };

                    var newDate = D_START.setDate(D_START.getDate() + 1);
                    D_START = new Date(newDate);
                }

                return gupEvents2
            }

            //console.log( parseJsonWeekday(monthOfPrices2) )
            //console.log( parseJsonWeekend(monthOfPrices2) )
            //console.log( parseJsonSpecialdays(monthOfPrices2) )


            /* select offer(s)
             -----------------------------------------------------------------*/
            var emptyObj = {};

            var initialLocaleCode = 'ru';

            var request = $.ajax({
                type: 'POST',
                contentType: "application/json; charset=utf-8",
                url: 'http://gup.com.ua:8184/api/rest/offersService/offer/read/all',
                data: JSON.stringify(emptyObj),
                error: function(offers) {
                    console.log(offers.responseText);
                },
                success: function(offers){
                    offerResult = JSON.parse( JSON.stringify(offers) );

                    // "id":"57f37a5e6032233325b9f8c9"
                    offerResult.forEach(function(el) {
                        if (el.offer.id  === '57f37a5e6032233325b9f8c9'){
                            $('#offers-selector').append('<option title="' + el.offer.id + '" selected>'+el.offer.title+'</option>')
                        }else{
                            $('#offers-selector').append('<option title="' + el.offer.id + '">'+el.offer.title+'</option>')
                        }
                    })
                    //////////////////////////////////////////////////////////////////////////////////////////
                    var index = document.getElementById('offers-selector').selectedIndex
                    var gupEventWeekday = parseJsonWeekday(offerResult[index].offer.monthOfPrices), gupEventWeekend = parseJsonWeekend(offerResult[index].offer.monthOfPrices), gupEventSpecialdays = parseJsonSpecialdays(offerResult[index].offer.monthOfPrices);
//                    gupEvents = gupEvents.concat(gupEventWeekday, gupEventWeekend, gupEventSpecialdays);
                    $('#offers-result2').html(offerResult[index].offer.id);
                    $('#offers-result3').html(offerResult[index].offer.userInfo.contactName);
                    $('#offers-result41').html(JSON.stringify(offerResult[index].offer.monthOfPrices) + '<br><br>');
                    $('#offers-result42').html(JSON.stringify(offerResult[index].offer.rents) + '<br>');
                }
            }).then(l=> {
                console.log( gupEvents )

                /* initialize the external events
                 -----------------------------------------------------------------*/
                $('#external-events .fc-event').each(function() {
                    // store data so the calendar knows to render an event upon drop
                    $(this).data('event', {
                        title: $.trim($(this).text()), // use the element's text as the event title
                        stick: true                    // maintain when user navigates (see docs on the renderEvent method)
                    });
                    // make the event draggable using jQuery UI
                    $(this).draggable({
                        zIndex: 999,
                        revert: true,                  // will cause the event to go back to its
                        revertDuration: 0              // original position after the drag
                    });
                });

                /* initialize the calendar
                 -----------------------------------------------------------------*/
                $('#calendar').fullCalendar({
                    theme: true,
                    header: {
                        left: 'prev,today,next',
                        center: 'title',
                        right: 'agendaDay,month' ////right: 'month,agendaWeek,agendaDay'
                    },
                    defaultDate: '2016-11-25',
                    locale: initialLocaleCode,
//                buttonIcons: false,      // show the prev/next text
                    weekNumbers: false,
                    editable: false,
                    navLinks: true,          // can click day/week names to navigate views
                    eventLimit: true,        // allow "more" link when too many events
                    businessHours: true,     // display business hours
                    displayEventTime: false, // don't show the time column in list view
                    droppable: true,         // this allows things to be dropped onto the calendar
                    dragRevertDuration: 0,
                    height: "auto",
                    googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE', // To make your own Google API key, follow the directions here: http://fullcalendar.io/docs/google_calendar/
                    events: gupEvents,
                    drop: function() {
                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    },
                    eventDragStop: function( event, jsEvent, ui, view ) {
                        if(isEventOverDiv(jsEvent.clientX, jsEvent.clientY)) {
                            $('#calendar').fullCalendar('removeEvents', event._id);
                            var el = $( "<div class='fc-event'>" ).appendTo( '#external-events-listing' ).text( event.title );
                            el.draggable({
                                zIndex: 999,
                                revert: true,
                                revertDuration: 0
                            });
                            el.data('event', { title: event.title, id :event.id, stick: true });
                        }
                    },
                    eventClick: function(event) {
                        // opens events in a popup window
                        window.open(event.url, 'gcalevent', 'width=700,height=600');
                        return false;
                    },
                    loading: function(bool) {
                        $('#loading').toggle(bool);
                    }
                });

                $( "#addPriceButton" ).bind('keypress', function(e) {
                    var code = e.keyCode || e.which;
                    if(code == 13) {
                        $('#external-events').append('<div class="fc-event">' + $(this).val() + ' $</div>');
                    }
                });
            });





            $('#offers-selector').change(function() {
                var index = document.getElementById('offers-selector').selectedIndex
                $('#offers-result2').html(offerResult[index].offer.id);
                $('#offers-result3').html(offerResult[index].offer.userInfo.contactName);

                if(offerResult[index].offer.monthOfPrices === undefined){
                    gupEvents = [];
                    $('#offers-result41').html('');
                }else{
                    $('#offers-result41').html(JSON.stringify(offerResult[index].offer.monthOfPrices) + '<br><br>');

                    var gupEventWeekday = parseJsonWeekday(offerResult[index].offer.monthOfPrices), gupEventWeekend = parseJsonWeekend(offerResult[index].offer.monthOfPrices), gupEventSpecialdays = parseJsonSpecialdays(offerResult[index].offer.monthOfPrices);
//                    gupEvents = gupEvents.concat(gupEventWeekday, gupEventWeekend, gupEventSpecialdays);
                }
                if(offerResult[index].offer.rents === undefined){
                    $('#offers-result42').html('');
                }else{
                    $('#offers-result42').html(JSON.stringify(offerResult[index].offer.rents) + '<br>');
                }
                //////////////////////////////////////////////////////////////
                console.log( gupEvents )

                /* change the calendar
                 -----------------------------------------------------------------*/

                $('#calendar').fullCalendar('destroy');
                $('#calendar').fullCalendar({
                    theme: true,
                    header: {
                        left: 'prev,today,next',
                        center: 'title',
                        right: 'agendaDay,month,listWeek' ////right: 'month,agendaWeek,agendaDay'
                    },
                    defaultDate: '2016-11-25',
                    locale: initialLocaleCode,
//                buttonIcons: false,      // show the prev/next text
                    weekNumbers: false,
                    editable: false,
                    navLinks: true,          // can click day/week names to navigate views
                    eventLimit: true,        // allow "more" link when too many events
                    businessHours: true,     // display business hours
                    displayEventTime: false, // don't show the time column in list view
                    droppable: true,         // this allows things to be dropped onto the calendar
                    dragRevertDuration: 0,
                    height: "auto",
                    googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE', // To make your own Google API key, follow the directions here: http://fullcalendar.io/docs/google_calendar/
                    events: gupEvents,
                    drop: function() {
                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    },
                    eventDragStop: function( event, jsEvent, ui, view ) {
                        if(isEventOverDiv(jsEvent.clientX, jsEvent.clientY)) {
                            $('#calendar').fullCalendar('removeEvents', event._id);
                            var el = $( "<div class='fc-event'>" ).appendTo( '#external-events-listing' ).text( event.title );
                            el.draggable({
                                zIndex: 999,
                                revert: true,
                                revertDuration: 0
                            });
                            el.data('event', { title: event.title, id :event.id, stick: true });
                        }
                    },
                    eventClick: function(event) {
                        // opens events in a popup window
                        window.open(event.url, 'gcalevent', 'width=700,height=600');
                        return false;
                    },
                    loading: function(bool) {
                        $('#loading').toggle(bool);
                    }
                });
                $('#calendar').fullCalendar('render');
            });





            var isEventOverDiv = function(x, y) {
                var external_events = $( '#external-events' );
                var offset = external_events.offset();
                offset.right = external_events.width() + offset.left;
                offset.bottom = external_events.height() + offset.top;

                // Compare
                if (x >= offset.left
                        && y >= offset.top
                        && x <= offset.right
                        && y <= offset .bottom) { return true; }
                return false;
            }

            // build the locale selector's options
            $.each($.fullCalendar.locales, function(localeCode) {
                $('#locale-selector').append(
                        $('<option/>')
                                .attr('value', localeCode)
                                .prop('selected', localeCode == initialLocaleCode)
                                .text(localeCode)
                );
            });
            // when the selected option changes, dynamically change the calendar option
            $('#locale-selector').on('change', function() {
                if (this.value) {
                    $('#calendar').fullCalendar('option', 'locale', this.value);
                }
            });
        });
    </script>
    <style>
        #calendar {
            min-width: 42%;
            max-width: 42%;
            margin: 0px auto;
            padding: 0 10px;
            /*float: left;*/
        }

        .fc-basic-view .fc-week-number,
        .fc-basic-view .fc-day-number {
            background: none;
            border: none;
        }
        .ui-widget-content a {
            color: #362b36;
            font-size: 24px;
            font-weight: bold;
            margin: 20%;
        }
        /*
                .fc-row .fc-content-skeleton td,
                .fc-row .fc-helper-skeleton td {
                    background: #33f;
                    border-radius: 35px;
                    padding: 0;
                }
                .fc table {
                    font-size: 16px;
                    width: 100%;
                }
                .fc td {
                    border-width: 0;
                }
        */

        #locale-selector {
            float: left;
            width: 150px;
            background: #3baae3 url("../../../resources/fullcalendar/lib/cupertino/images/ui-bg_glass_50_3baae3_1x400.png") repeat-x scroll 50% 50%;
            border: 1px solid #2694e8;
            border-radius: 5px;
            padding: 0px 8px;
            font-weight: bold;
            font-size: 20px;
            color: #ffffff;
        }

        #offers-selector {
            background: #3baae3 url("../../../resources/fullcalendar/lib/cupertino/images/ui-bg_glass_50_3baae3_1x400.png") repeat-x scroll 50% 50%;
            border: 1px solid #2694e8;
            border-radius: 5px;
            color: #ffffff;
            float: left;
            font-size: 20px;
            padding: 2px 8px;
            width: 700px;
        }

        legend {
            margin-bottom: 0;
        }

        #external-events {
            float: left;
            width: 160px; /*width: 150px;*/
            padding: 0 10px;
            border: 1px solid #aed0ea;
            background: #deedf7;
            text-align: left;
            margin-top: 52px;
            margin-left: 0; /*margin-left: -150px;*/
        }
        #external-events h4 {
            font-size: 16px;
            margin-top: 0;
            padding-top: 1em;
        }
        #external-events .fc-event {
            margin: 10px 0;
            cursor: pointer;
        }
        #external-events p {
            margin: 1.5em 0;
            font-size: 11px;
            color: #666;
        }
        #external-events p input {
            margin: 0;
            vertical-align: middle;
        }
    </style>
</head>
<body>

<div id="wrapper">

    <!-- Navigation -->
    <jsp:include page="/WEB-INF/templates/admin-panel/admin-left-bar.jsp"/>
    <!-- Navigation -->

    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header"><u>Арендатор</u> &rarr; <a href="admin-landlord-rents" style="font-size:40px;">Арендодатель</a></h1>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">

                <table>
                    <tr>
                        <td> &nbsp;&nbsp; Объявление: &nbsp;&nbsp; </td>
                        <td> <select id="offers-selector" name="offers-selector"></select> </td>
                        <td> &nbsp;&nbsp; | &nbsp;&nbsp; </td>
                        <td> Арендодатель: &nbsp;&nbsp; </td>
                        <td> <legend id="offers-result3"></legend> </td>
                    </tr>
                </table>

                <br>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="dataTable_wrapper">
                            <!--<div id='external-events'></div>-->
                            <div id='calendar'></div>
                        </div>
                    </div>
                </div>
                <br>

                <fieldset>
                    <legend id="offers-result2"></legend>
                    <div id='monthOfPrices'></div>
                    <!--<font color="#2980b9" id="offers-result41"></font>-->
                    <font color="#FF5733" id="offers-result42"></font>
                    <!--<font color="gray" id="offers-result1"></font>-->
                </fieldset>
            </div>
        </div>
    </div>
</div>

</body>

<!-- Bottom Links -->
<jsp:include page="/WEB-INF/templates/admin-panel/admin-bottom-links2.jsp"/>
<!-- Bottom Links -->
</html>
