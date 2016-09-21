var idCorrect = [];
var userNames = [];
var users;

$(document).ready(function () {
    var data;
    var filterOptions = new Object();
    filterOptions.skip = 0;
    filterOptions.limit = 20;
    filterOptions.userRoles = ['ROLE_ADMIN', 'ROLE_MODERATOR'];

    $('#myModal').on('shown.bs.modal', function () {
        $('#myInput').focus()
    });

    $('#deleteModal').on('shown.bs.modal', function () {
        $('#myInput').focus()
    });

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:8082/api/rest/admin/profile/read/all",
        data: JSON.stringify(filterOptions),
        success: function (response) {

            console.log(response)


            users = jQuery.extend(true, {}, response.entities);
            data = response.entities;
            for (var k = 0; k < data.length; k++){
                userNames.push(data[k].email);
            }
            for (var i = 0; i < data.length; i++) {
                if (data[i].contact !== null) {
                    if (data[i].imgId !== null &&   data[i].imgId.length > 2) {
                        data[i].imgId = '<img src="/api/rest/fileStorage/PROFILE/file/read/id/' + data[i].imgId + '" width="100" height="100">';
                    }
                    else {
                        data[i].imgId = '<img src="/resources/images/no_photo.jpg" width="100" height="100">';
                    }
                }
                else {
                    data[i].contact = {};
                    data[i].imgId = '<img src="/resources/images/no_photo.jpg" width="100" height="100">';
                }
            }

            var admins = [];
            var moderators = [];
            for (var m = 0; m < data.length; m++) {
                if(data[m].userRoles!==undefined && data[m].userRoles!==null){
                    for (var n = 0; n < data[m].userRoles.length; n++) {
                        if (data[m].userRoles[n] === 'ROLE_ADMIN') {
                            admins.push(data[m]);
                        }
                        if (data[m].userRoles[n] === 'ROLE_MODERATOR') {
                            moderators.push(data[m]);
                        }
                    }
                }

            }

            var tableAdmins = $('#admins').DataTable({
                select: {
                    style: 'single'
                },
                data: admins,
                "columns": [
                    {"data": "imgId"},
                    {"data": "email"},
                    {"data": "createdDate"}
                ],
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.9/i18n/Russian.json"
                }
            });

            tableAdmins
                .on('select', function (e, dt, type, indexes) {
                    var rowData = table.rows(indexes).data().toArray();
                    $("input[name='transactionId']").attr("value", rowData[0].id);
                    $('#inp').removeAttr("readonly");
                    $('#cancelBtn').attr("class", "btn btn-danger");
                })
                .on('deselect', function (e, dt, type, indexes) {
                    $("input[name='transactionId']").attr("value", "");
                    $('#inp').attr("readonly", "readonly");
                    $('#cancelBtn').attr("class", "btn btn-danger disabled");
                });

            var tableModerators = $('#moderators').DataTable({
                select: {
                    style: 'single'
                },
                data: moderators,
                "columns": [
                    {"data": "imgId"},
                    {"data": "email"},
                    {"data": "createdDate"}
                ],
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.10.9/i18n/Russian.json"
                }
            });

            tableModerators
                .on('select', function (e, dt, type, indexes) {
                    var rowData = table.rows(indexes).data().toArray();
                    $("input[name='transactionId']").attr("value", rowData[0].id);
                    $('#inp').removeAttr("readonly");
                    $('#cancelBtn').attr("class", "btn btn-danger");
                })
                .on('deselect', function (e, dt, type, indexes) {
                    $("input[name='transactionId']").attr("value", "");
                    $('#inp').attr("readonly", "readonly");
                    $('#cancelBtn').attr("class", "btn btn-danger disabled");
                });

            var logins = new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.whitespace,
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                // `states` is an array of state names defined in "The Basics"
                local: userNames
            });

            $('#scrollable-dropdown-menu .typeahead').typeahead(null, {
                name: 'logins',
                limit: 10,
                source: logins
            });

        }

    });


});

$('#typeahead').blur(function(){
    var user = {};
    for(var i in users){
        if (users[i].email === this.value){
            user = users[i];
            break;
        }
    }

    $('.ch2').prop("checked",false);

    for(var j in user.userRoles){
        switch (user.userRoles[j]) {
            case 'ROLE_ADMIN':
                $('#adminCheck2').prop("checked",true);
                break;
            case 'ROLE_SUPPORT':
                $('#supportCheck2').prop("checked",true);
                break;
            case 'ROLE_MODERATOR':
                $('#moderatorCheck2').prop("checked",true);
                break;
            case 'ROLE_USER':
                $('#userCheck2').prop("checked",true);
                break;
            case 'ROLE_ANONYMOUS':
                $('#anonymousCheck2').prop("checked",true);
                break;
        }
    }

    $('#update').click(function(){
        user.userRoles = [];

        if($('#adminCheck2').prop("checked")){
            user.userRoles.push('ROLE_ADMIN');
        }
        if($('#supportCheck2').prop("checked")){
            user.userRoles.push('ROLE_SUPPORT');
        }
        if($('#moderatorCheck2').prop("checked")){
            user.userRoles.push('ROLE_MODERATOR');
        }
        if($('#userCheck2').prop("checked")){
            user.userRoles.push('ROLE_USER');
        }
        if($('#anonymousCheck2').prop("checked")){
            user.userRoles.push('ROLE_ANONYMOUS');
        }

        $.ajax({
            type: "POST",
            url: "/api/rest/profilesService/profile/updateByAdmin",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(user),
            success: function (response) {
                window.location.href = '/admin-admins';
            }
        });
    });

});

$('#create').click(function(){
    var user = {};
    var login;
    var password;
    var roles = [];
    login =$('#newLogin').val();
    password =$('#newPassword').val();
    if($('#adminCheck').prop("checked")){
        roles.push('ROLE_ADMIN');
    }
    if($('#supportCheck').prop("checked")){
        roles.push('ROLE_SUPPORT');
    }
    if($('#moderatorCheck').prop("checked")){
        roles.push('ROLE_MODERATOR');
    }
    if($('#userCheck').prop("checked")){
        roles.push('ROLE_USER');
    }
    if($('#anonymousCheck').prop("checked")){
        roles.push('ROLE_ANONYMOUS');
    }

    user.username = login;
    user.email = login;
    user.password = password;
    user.userRoles = roles;

    $.ajax({
        type: "POST",
        url: "/api/rest/profilesService/profile/create",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(user),
        success: function (response) {
            window.location.href = '/admin-admins';
        }
    });
});