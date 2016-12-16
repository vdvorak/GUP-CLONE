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

    <title>Арендодатель | Панель управления</title>
    <meta charset='utf-8' />

    <!-- Links -->
    <jsp:include page="/WEB-INF/templates/admin-panel/admin-top-links.jsp"/>
    <!-- Links -->

    <script>
        var gupEvents = [];
        var offerResult;

        $(document).ready(function() {

            var zone = "05:30";

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
                    gupEvents = gupEvents.concat(gupEventWeekday, gupEventWeekend, gupEventSpecialdays);
                    $('#offers-result2').html(offerResult[index].offer.id);
                    $('#offers-result3').html(offerResult[index].offer.userInfo.contactName);
                    $('#offers-result41').html(JSON.stringify(offerResult[index].offer.monthOfPrices) + '<br><br>');
                    $('#offers-result42').html(JSON.stringify(offerResult[index].offer.rents) + '<br>');

                    $('#external-events').html('<p>'
                    + '<center><img src="../../../resources/fullcalendar/img/trashcan.png" id="trash" alt=""/></center>'
                    + '</p><p style="padding-bottom: 16px; border-bottom:1px solid #aed0ea;">'
                    + '<select id="set-price">'
                    + '<option></option>'
                    + '<option value="single">Единная цена</option>'
                    + '<option value="weekend">Цена на выходные</option>'
                    + '<option value="weekday">Цена на будние</option>'
                    + '<option value="specialdays" selected>Специальная цена</option>'
                    + '</select>'
                    + ' &nbsp; <input type="checkbox" id="drop-remove" checked="checked" style="float:right; margin-top:2px;" />'
                    + '<br/><br/><input type="text" id="addPriceButton" style="width:100%" value="0" />'
                    + '</p>');
                    $('#external-events').append('<div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на выходные">' + offerResult[index].offer.monthOfPrices.weekend.price + '</div>');
                    $('#external-events').append('<div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на будние">' + offerResult[index].offer.monthOfPrices.weekday.price + '</div>');
                    $('#external-events').append('<div class="fc-event" title="Специальная цена">' + offerResult[index].offer.monthOfPrices.specialdays[0].price + '</div>');
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
                        right: 'agendaDay,month,listWeek' ////right: 'month,agendaWeek,agendaDay'
                    },
                    defaultDate: '2016-11-25',
                    locale: initialLocaleCode,
//                buttonIcons: false,      // show the prev/next text
                    weekNumbers: false,
                    editable: true,
                    navLinks: true,          // can click day/week names to navigate views
                    eventLimit: true,        // allow "more" link when too many events
                    businessHours: true,     // display business hours
                    displayEventTime: false, // don't show the time column in list view
                    droppable: true,         // this allows things to be dropped onto the calendar
                    dragRevertDuration: 0,
                    googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE', // To make your own Google API key, follow the directions here: http://fullcalendar.io/docs/google_calendar/
                    events: gupEvents,
                    drop: function() {
                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    },
                    eventReceive: function(event){
                        var title = event.title;
                        var start = event.start.format("YYYY-MM-DD[T]HH:mm:SS");
                        $.ajax({
                            url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                            data: 'type=new&title='+title+'&startdate='+start+'&zone='+zone,
                            type: 'POST',
                            dataType: 'json',
                            success: function(response){
                                event.id = response.eventid;
                                $('#calendar').fullCalendar('updateEvent',event);
                            },
                            error: function(e){
                                console.log(e.responseText);
                            }
                        });
                        $('#calendar').fullCalendar('updateEvent',event);
                        console.log(event);
                    },
                    eventDrop: function(event, delta, revertFunc) {
                        var title = event.title;
                        var start = event.start.format();
                        var end = (event.end == null) ? start : event.end.format();
                        $.ajax({
                            url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                            data: 'type=resetdate&title='+title+'&start='+start+'&end='+end+'&eventid='+event.id,
                            type: 'POST',
                            dataType: 'json',
                            success: function(response){
                                if(response.status != 'success')
                                    revertFunc();
                            },
                            error: function(e){
                                revertFunc();
                                alert('Error processing your request: '+e.responseText);
                            }
                        });
                    },
                    /*
                     eventClick: function(event) {
                     // opens events in a popup window
                     window.open(event.url, 'gcalevent', 'width=700,height=600');
                     return false;
                     },
                     */
                    eventClick: function(event, jsEvent, view) {
                        console.log(event.id);
                        var title = prompt('Event Title:', event.title, { buttons: { Ok: true, Cancel: false} });
                        if (title){
                            event.title = title;
                            console.log('type=changetitle&title='+title+'&eventid='+event.id);
                            $.ajax({
                                url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                                data: 'type=changetitle&title='+title+'&eventid='+event.id,
                                type: 'POST',
                                dataType: 'json',
                                success: function(response){
                                    if(response.status == 'success')
                                        $('#calendar').fullCalendar('updateEvent',event);
                                },
                                error: function(e){
                                    alert('Error processing your request: '+e.responseText);
                                }
                            });
                        }
                    },
                    eventResize: function(event, delta, revertFunc) {
                        console.log(event);
                        var title = event.title;
                        var end = event.end.format();
                        var start = event.start.format();
                        $.ajax({
                            url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                            data: 'type=resetdate&title='+title+'&start='+start+'&end='+end+'&eventid='+event.id,
                            type: 'POST',
                            dataType: 'json',
                            success: function(response){
                                if(response.status != 'success')
                                    revertFunc();
                            },
                            error: function(e){
                                revertFunc();
                                alert('Error processing your request: '+e.responseText);
                            }
                        });
                    },
                    /*
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
                     */
                    eventDragStop: function (event, jsEvent, ui, view) {
                        if(isEventOverDiv(jsEvent.clientX, jsEvent.clientY)) {
                            var con = confirm('Are you sure to delete this event permanently?');
                            if(con == true) {
                                $('#calendar').fullCalendar('removeEvents', event._id);
                                var el = $( "<div class='fc-event'>" ).appendTo( '#external-events-listing' ).text( event.title );
                                el.draggable({
                                    zIndex: 999,
                                    revert: true,
                                    revertDuration: 0
                                });
                                el.data('event', { title: event.title, id :event.id, stick: true });
                                //////////////////////////////////////////////////////////////////////
                                $.ajax({
                                    url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                                    data: 'type=remove&eventid='+event.id,
                                    type: 'POST',
                                    dataType: 'json',
                                    success: function(response){
                                        console.log(response);
                                        if(response.status == 'success'){
                                            $('#calendar').fullCalendar('removeEvents');
                                            getFreshEvents();
                                        }
                                    },
                                    error: function(e){
                                        alert('Error processing your request: '+e.responseText);
                                    }
                                });
                            }
                        }
                    },
                    loading: function(bool) {
                        $('#loading').toggle(bool);
                    }
                });

                /////////////////////////////////////////////////////////
                $( "#addPriceButton" ).bind('keypress', function(e) {
                    var code = e.keyCode || e.which;
                    if(code == 13) {
                        if (document.getElementById('set-price').value === 'specialdays'){
                            $('#external-events').append('<div class="fc-event" title="Специальная цена">' + $(this).val() + '</div>');
                        }
                        if (document.getElementById('set-price').value === 'weekday'){
                            $('#external-events').append('<div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на будние">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        if (document.getElementById('set-price').value === 'weekend'){
                            $('#external-events').append('<div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на выходные">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        if (document.getElementById('set-price').value === 'single'){
                            $('#external-events').append('<div class="" style="background:#ada; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Единная цена">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }

                        /////////////////////////////////////////////////
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
                    }
                });
            });



            $('#savePriceButton').click(function() {
                console.log('type=resetdate&title=1000&start=2016-11-02&end=2016-11-04&eventid=undefined');
                $.ajax({
                    url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                    data: 'type=resetdate&title=1000&start=2016-11-02&end=2016-11-04&eventid=undefined',
                    type: 'POST',
                    dataType: 'json',
                    success: function(response){
                        if(response.status != 'success')
                            revertFunc();
                    },
                    error: function(e){
                        revertFunc();
                        alert('Error processing your request: '+e.responseText);
                    }
                });
                $("#savePriceButton").attr('class', 'btn btn-primary disabled');
                $("#cancelPriceButton").attr('class', 'btn btn-primary disabled');
            });

            $('#cancelPriceButton').click(function() {
                index = document.getElementById('offers-selector').selectedIndex
                $('#external-events').html('<p>'
                + '<center><img src="../../../resources/fullcalendar/img/trashcan.png" id="trash" alt=""/></center>'
                + '</p><p style="padding-bottom: 16px; border-bottom:1px solid #aed0ea;">'
                + '<select id="set-price">'
                + '<option></option>'
                + '<option value="single">Единная цена</option>'
                + '<option value="weekend">Цена на выходные</option>'
                + '<option value="weekday">Цена на будние</option>'
                + '<option value="specialdays" selected>Специальная цена</option>'
                + '</select>'
                + ' &nbsp; <input type="checkbox" id="drop-remove" checked="checked" style="float:right; margin-top:2px;" />'
                + '<br/><br/><input type="text" id="addPriceButton" style="width:100%" value="0" />'
                + '</p>');
                $('#external-events').append('<div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на выходные">' + offerResult[index].offer.monthOfPrices.weekend.price + '</div>');
                $('#external-events').append('<div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на будние">' + offerResult[index].offer.monthOfPrices.weekday.price + '</div>');
                $('#external-events').append('<div class="fc-event" title="Специальная цена">' + offerResult[index].offer.monthOfPrices.specialdays[0].price + '</div>');
                $("#savePriceButton").attr('class', 'btn btn-primary disabled');
                $("#cancelPriceButton").attr('class', 'btn btn-primary disabled');
                /////////////////////////////////////////////////
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
                /////////////////////////////////////////////////
                $( "#addPriceButton" ).bind('keypress', function(e) {
                    var code = e.keyCode || e.which;
                    if(code == 13) {
                        if (document.getElementById('set-price').value === 'specialdays'){
                            $('#external-events').append('<div class="fc-event" title="Специальная цена">' + $(this).val() + '</div>');
                        }
                        if (document.getElementById('set-price').value === 'weekday'){
                            $('#external-events').append('<div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на будние">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        if (document.getElementById('set-price').value === 'weekend'){
                            $('#external-events').append('<div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на выходные">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        if (document.getElementById('set-price').value === 'single'){
                            $('#external-events').append('<div class="" style="background:#ada; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Единная цена">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        /////////////////////////////////////////////////
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
                    }
                });
            });



            $('#offers-selector').change(function() {
                var index = document.getElementById('offers-selector').selectedIndex
                $('#offers-result2').html(offerResult[index].offer.id);
                $('#offers-result3').html(offerResult[index].offer.userInfo.contactName);

                $('#external-events').html('<p>'
                + '<center><img src="../../../resources/fullcalendar/img/trashcan.png" id="trash" alt=""/></center>'
                + '</p><p style="padding-bottom: 16px; border-bottom:1px solid #aed0ea;">'
                + '<select id="set-price">'
                + '<option></option>'
                + '<option value="single">Единная цена</option>'
                + '<option value="weekend">Цена на выходные</option>'
                + '<option value="weekday">Цена на будние</option>'
                + '<option value="specialdays" selected>Специальная цена</option>'
                + '</select>'
                + ' &nbsp; <input type="checkbox" id="drop-remove" checked="checked" style="float:right; margin-top:2px;" />'
                + '<br/><br/><input type="text" id="addPriceButton" style="width:100%" value="0" />'
                + '</p>');
                if(offerResult[index].offer.monthOfPrices === undefined){
                    gupEvents = [];
                    $('#offers-result41').html('');
                }else{
                    $('#offers-result41').html(JSON.stringify(offerResult[index].offer.monthOfPrices) + '<br><br>');

                    var gupEventWeekday = parseJsonWeekday(offerResult[index].offer.monthOfPrices), gupEventWeekend = parseJsonWeekend(offerResult[index].offer.monthOfPrices), gupEventSpecialdays = parseJsonSpecialdays(offerResult[index].offer.monthOfPrices);
                    gupEvents = gupEvents.concat(gupEventWeekday, gupEventWeekend, gupEventSpecialdays);

                    $('#external-events').append('<div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на выходные">' + offerResult[index].offer.monthOfPrices.weekend.price + '</div>');
                    $('#external-events').append('<div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на будние">' + offerResult[index].offer.monthOfPrices.weekday.price + '</div>');
                    $('#external-events').append('<div class="fc-event" title="Специальная цена">' + offerResult[index].offer.monthOfPrices.specialdays[0].price + '</div>');
                }
                if(offerResult[index].offer.rents === undefined){
                    $('#offers-result42').html('');
                }else{
                    $('#offers-result42').html(JSON.stringify(offerResult[index].offer.rents) + '<br>');
                }
                $("#savePriceButton").attr('class', 'btn btn-primary disabled');
                $("#cancelPriceButton").attr('class', 'btn btn-primary disabled');
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
                    editable: true,
                    navLinks: true,          // can click day/week names to navigate views
                    eventLimit: true,        // allow "more" link when too many events
                    businessHours: true,     // display business hours
                    displayEventTime: false, // don't show the time column in list view
                    droppable: true,         // this allows things to be dropped onto the calendar
                    dragRevertDuration: 0,
                    googleCalendarApiKey: 'AIzaSyDcnW6WejpTOCffshGDDb4neIrXVUA1EAE', // To make your own Google API key, follow the directions here: http://fullcalendar.io/docs/google_calendar/
                    events: gupEvents,
                    drop: function() {
                        // is the "remove after drop" checkbox checked?
                        if ($('#drop-remove').is(':checked')) {
                            // if so, remove the element from the "Draggable Events" list
                            $(this).remove();
                        }
                    },
                    eventReceive: function(event){
                        var title = event.title;
                        var start = event.start.format("YYYY-MM-DD[T]HH:mm:SS");
                        $.ajax({
                            url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                            data: 'type=new&title='+title+'&startdate='+start+'&zone='+zone,
                            type: 'POST',
                            dataType: 'json',
                            success: function(response){
                                event.id = response.eventid;
                                $('#calendar').fullCalendar('updateEvent',event);
                            },
                            error: function(e){
                                console.log(e.responseText);
                            }
                        });
                        $('#calendar').fullCalendar('updateEvent',event);
                        console.log(event);
                    },
                    eventDrop: function(event, delta, revertFunc) {
                        var title = event.title;
                        var start = event.start.format();
                        var end = (event.end == null) ? start : event.end.format();
                        $.ajax({
                            url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                            data: 'type=resetdate&title='+title+'&start='+start+'&end='+end+'&eventid='+event.id,
                            type: 'POST',
                            dataType: 'json',
                            success: function(response){
                                if(response.status != 'success')
                                    revertFunc();
                            },
                            error: function(e){
                                revertFunc();
                                alert('Error processing your request: '+e.responseText);
                            }
                        });
                    },
                    /*
                     eventClick: function(event) {
                     // opens events in a popup window
                     window.open(event.url, 'gcalevent', 'width=700,height=600');
                     return false;
                     },
                     */
                    eventClick: function(event, jsEvent, view) {
                        console.log(event.id);
                        var title = prompt('Event Title:', event.title, { buttons: { Ok: true, Cancel: false} });
                        if (title){
                            event.title = title;
                            console.log('type=changetitle&title='+title+'&eventid='+event.id);
                            $.ajax({
                                url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                                data: 'type=changetitle&title='+title+'&eventid='+event.id,
                                type: 'POST',
                                dataType: 'json',
                                success: function(response){
                                    if(response.status == 'success')
                                        $('#calendar').fullCalendar('updateEvent',event);
                                },
                                error: function(e){
                                    alert('Error processing your request: '+e.responseText);
                                }
                            });
                        }
                    },
                    eventResize: function(event, delta, revertFunc) {
                        console.log(event);
                        var title = event.title;
                        var end = event.end.format();
                        var start = event.start.format();
                        $.ajax({
                            url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                            data: 'type=resetdate&title='+title+'&start='+start+'&end='+end+'&eventid='+event.id,
                            type: 'POST',
                            dataType: 'json',
                            success: function(response){
                                if(response.status != 'success')
                                    revertFunc();
                            },
                            error: function(e){
                                revertFunc();
                                alert('Error processing your request: '+e.responseText);
                            }
                        });
                    },
                    /*
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
                     */
                    eventDragStop: function (event, jsEvent, ui, view) {
                        if(isEventOverDiv(jsEvent.clientX, jsEvent.clientY)) {
                            var con = confirm('Are you sure to delete this event permanently?');
                            if(con == true) {
                                $('#calendar').fullCalendar('removeEvents', event._id);
                                var el = $( "<div class='fc-event'>" ).appendTo( '#external-events-listing' ).text( event.title );
                                el.draggable({
                                    zIndex: 999,
                                    revert: true,
                                    revertDuration: 0
                                });
                                el.data('event', { title: event.title, id :event.id, stick: true });
                                //////////////////////////////////////////////////////////////////////
                                $.ajax({
                                    url: 'http://956804.rb242731.web.hosting-test.net/process.php', // !!!!!
                                    data: 'type=remove&eventid='+event.id,
                                    type: 'POST',
                                    dataType: 'json',
                                    success: function(response){
                                        console.log(response);
                                        if(response.status == 'success'){
                                            $('#calendar').fullCalendar('removeEvents');
                                            getFreshEvents();
                                        }
                                    },
                                    error: function(e){
                                        alert('Error processing your request: '+e.responseText);
                                    }
                                });
                            }
                        }
                    },
                    loading: function(bool) {
                        $('#loading').toggle(bool);
                    }
                });
                $('#calendar').fullCalendar('render');
                /////////////////////////////////////////////////////////
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
                /////////////////////////////////////////////////////////
                $( "#addPriceButton" ).bind('keypress', function(e) {
                    var code = e.keyCode || e.which;
                    if(code == 13) {
                        if (document.getElementById('set-price').value === 'specialdays'){
                            $('#external-events').append('<div class="fc-event" title="Специальная цена">' + $(this).val() + '</div>');
                        }
                        if (document.getElementById('set-price').value === 'weekday'){
                            $('#external-events').append('<div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на будние">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        if (document.getElementById('set-price').value === 'weekend'){
                            $('#external-events').append('<div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Цена на выходные">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }
                        if (document.getElementById('set-price').value === 'single'){
                            $('#external-events').append('<div class="" style="background:#ada; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;" title="Единная цена">' + $(this).val() + '</div>');
                            $("#savePriceButton").attr('class', 'btn btn-primary');
                            $("#cancelPriceButton").attr('class', 'btn btn-primary');
                        }

                        /////////////////////////////////////////////////
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
                    }
                });
            });







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
            min-width: 85%;
            max-width: 85%;
            margin: 0px auto;
            padding: 0 10px;
            float: right;
        }

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
            /*width: 15%;*/ /*width: 150px;*/
            padding: 0 10px;
            border: 1px solid #aed0ea;
            background: #deedf7;
            text-align: left;
            /*margin-top: 52px;*/
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
                <h1 class="page-header"><a href="admin-tenant-rents" style="font-size:40px;">Арендатор</a> &larr; <u>Арендодатель</u></h1>
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
                            <table style="float:left;">
                                <tr><td>
                                    <div class="" style="background:#aca; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;">Цена на выходные</div>
                                    <div class="" style="background:#aba; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;">Цена на будние</div>
                                    <div class="" style="background:#ada; margin:0px 0px 10px; padding:0px 3px; color:#fff; font-size:12px;">Единная цена</div>
                                    <div class="fc-event" style="padding:0px 3px; color:#fff; font-size:12px;">Специальная цена</div>
                                </td></tr>
                                <tr><td> <br/> </td></tr>
                                <tr><td> <div id='external-events'></div> </td></tr>
                                <tr><td> <br/> </td></tr>
                                <tr><td> <button id="savePriceButton" class="btn btn-primary disabled" style="margin-left:20%; width:60%;">Применить</button>
                                </td></tr>
                                <tr><td> <br/> </td></tr>
                                <tr><td> <button id="cancelPriceButton" class="btn btn-primary disabled" style="margin-left:20%; width:60%;">Отменить</button>
                                </td></tr>
                            </table>
                            <div id='calendar'></div>
                        </div>
                    </div>
                </div>
                <br>

                <!--
                <fieldset>
                <legend id="offers-result2"></legend>
                <div id='monthOfPrices'></div>
                <font color="#2980b9" id="offers-result41"></font>
                <font color="#FF5733" id="offers-result42"></font>
                -->
                <!--<font color="gray" id="offers-result1"></font>-->
                <!--
                </fieldset>
                -->
            </div>
        </div>
    </div>
</div>

</body>

<!-- Bottom Links -->
<jsp:include page="/WEB-INF/templates/admin-panel/admin-bottom-links2.jsp"/>
<!-- Bottom Links -->
</html>