console.log(graph);

//color array
var colorarr = ["#99ccff", "#cc66ff", "#ff99cc", "#ff9966", "#ffd11a", "#99ff33", "#66ff66", "#00ff99", "#009999",
    "#0099cc", "#cc6699", "#a31aff", "#cccc00", "#73e600", "#00ffff", "#ffcc99", "#cc00cc", "#009999"];
var mapcolor = {};

if (algorithm == "Hill Climbing") {
    $("#hc-nav").addClass("active");
} else if (algorithm == "Simulated Annealing") {
    $("#sa-nav").addClass("active");
} else if (algorithm == "Genetic Algorithm") {
    $("#ga-nav").addClass("active");
}

//Assign slot
$(document).ready(function () {
    //variables
    var slot;
    var sss;
    var len = graph.variables.length;
    var dur;
    var x;
    var found;
    var matconf = {};

    //move first index to last index and move index n to index n-1
    function pushSwitch(conf) {
        var tempconf;
        tempconf = conf[0];
        for (i = 0; i < conf.length - 1; i++) {
            conf[i] = conf[i + 1];
        }
        conf[conf.length - 1] = tempconf;
    }

    //push selected course to the top array and show it
    function pushCourse(course, conf, confName) {
        //course must be in switch
        if (conf.includes(course)) {
            while (conf[0] !== course) {
                pushSwitch(conf);
            }
            $("#"+confName).html(conf[0]);
            $("#"+confName).css("background-color", mapcolor[course]);
            $("#"+confName).addClass("highlight");
        }
    }

    for (i = 0; i < len; i++) {
        dur = graph.variables[i].currDomain.duration;
        x = 0;
        for (j = 0; j < dur; j++) {
            slot = "tb" + graph.variables[i].currDomain.day;
            if (graph.variables[i].currDomain.startTime + x < 10) {
                slot += 0;
            }
            sss = slot + (graph.variables[i].currDomain.startTime + x);
            x++;
            if ($("#"+sss).html() == "") {
                $("#"+sss).html("<div>"+graph.variables[i].courseName+" - "+graph.variables[i].currDomain.room+"</div>");
            } else {
                //check for matconf obj
                if (typeof matconf[sss] === 'undefined') {
                    matconf[sss] = [];
                    matconf[sss].push($("#"+sss).html());
                    matconf[sss].push("<div>"+graph.variables[i].courseName+" - "+graph.variables[i].currDomain.room+"</div>");

                    //alert conflict error
                    $("#"+sss).addClass("conflict");
                } else {
                    matconf[sss].push("<div>"+graph.variables[i].courseName+" - "+graph.variables[i].currDomain.room+"</div>");
                }
                //check if room clashes
                found = false;
                for (k = 0; k < matconf[sss].length-1; k++) {
                    for (l = k+1; l < matconf[sss].length; l++) {
                        if (matconf[sss][k].substr(14) == matconf[sss][l].substr(14)) {
                            $("#"+sss).addClass('conflict-room');
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
            $("#"+sss).addClass("draggable");
        }
    }

    console.log(matconf);
    for (i = 0; i < graph.variables.length; i++) {
        mapcolor["<div>"+graph.variables[i].courseName+" - "+graph.variables[i].currDomain.room+"</div>"] = colorarr[i];
        $("td:contains("+graph.variables[i].courseName+" - "+graph.variables[i].currDomain.room+")").css("background-color", colorarr[i]);
    }

    $(document).on("dblclick", "td:nth-child(n+2) > div", function () {
        var courseShow = "<div>"+$(this).text()+"</div>";
        if ($(this).parent().hasClass("conflict")) {
            var idConf = $(this).parent().attr('id');
            pushSwitch(matconf[idConf]);
            courseShow = matconf[idConf][0];
        }
        for (var ab in matconf) {
            var j = ab;
            pushCourse(courseShow,matconf[j],j);
        }
        $(".invisible").html(courseShow);
        $("td").removeClass("highlight");
        $("td:nth-child(n+2):contains("+$(".invisible").text()+")").addClass("highlight");
    });

    $(document).on("click", "td:nth-child(n+2) > div", function () {
        $(".highlight").removeClass("highlight");
        $(this).toggleClass("highlight");
        if ($(this).html() !== "") {
            $("td:nth-child(n+2):contains("+$(this).html()+")").addClass("highlight");
        }
    });

    //drag and drop
    $(function() {
        function checkAllowDrop(dropID, dragID, countDrag, hour) {
            var tbID;
            var hrstr;
            if (17 - hour - countDrag + 1 >= 0) { //check if course won't end after 18.00
                // Check domain
                /* for (i = 0; i < countDrag; i++) {
                    if (hour + i < 10) {
                        hrstr = "0" + (hour + i);
                    } else {
                        hrstr = "" + (hour + i);
                    }
                    tbID = "#" + dropID.slice(0,3) + hrstr;
                    if (($(tbID).text() !== "") && ($(tbID).text() !== $("#"+dragID).text())) {
                        return false;
                    }
                } */
                return true;
            } else {
                return false;
            }
        }

        $(".draggable").draggable({
            revert: 'invalid',
            snap: 'td:nth-child(n+2)',
            helper: 'clone',
            zIndex: 100,
            start: function( event, ui ) {
                //make helper same height as td
                $(ui.helper).css('height', $(this).height());
                $(ui.helper).css('width', $(this).width());
                $(ui.helper).addClass('highlight');
            }
        });

        $("td:nth-child(n+2)").droppable({
            accept: 'td:nth-child(n+2)',
            drop: function(event, ui) {
                var target = $(event.target),
                    drag = $(ui.draggable),
                    targetText = "",
                    dragText = "",
                    duration = 0;

                var countDrag = 0;
                var hour = parseInt(target.attr('id').substr(-2,2));
                for (i = 0; i < graph.variables.length; i++) {
                    if ((graph.variables[i].courseName + " - " + graph.variables[i].currDomain.room) == drag.text()) {
                        countDrag = graph.variables[i].currDomain.duration;
                        break;
                    }
                }

                if(/*((target.text().trim() === "") || (target.text().trim() === drag.text().trim())) && */(checkAllowDrop(target.attr('id'), drag.attr('id'), countDrag,hour))){
                    //store original target, drag text
                    targetText = "";
                    dragText = "<div>"+drag.text().trim()+"</div>";

                    //change html for each element
                    $("td:nth-child(n+2):contains("+drag.text().trim()+"):not(.conflict)").draggable({disabled: true});
                    /* target.html(dragText);
                    target.css("background-color", drag.css("background-color"));
                    drag.html(targetText);
                    drag.css("background-color", "#fbff9c"); */

                    //clean another slots
                    for (var ab in matconf) {
                        var j = ab;
                        if (matconf[j].includes(dragText)){
                            if (matconf[j].indexOf(dragText) == 0) {
                                pushSwitch(matconf[j]);
                                matconf[j].pop();
                                $("#"+j).html(matconf[j][0]).css("background-color", mapcolor[matconf[j][0]]);
                            } else {
                                matconf[j].splice(matconf[j].indexOf(dragText), 1);
                            }

                            //check to eliminate conflict and conflict-room
                            if (matconf[j].length == 1) {
                                $("#"+j).removeClass('conflict conflict-room');
                                delete matconf[j];
                            } else {
                                found = false;
                                for (k = 0; k < matconf[j].length-1; k++) {
                                    for (l = k+1; l < matconf[j].length; l++) {
                                        if (matconf[j][k].substr(14) == matconf[j][l].substr(14)) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (found) break;
                                }
                                if (!(found)) {
                                    $("#"+j).removeClass('conflict-room');
                                }
                            }
                        }
                    }
                    $('.invisible').html(dragText);
                    $("td:nth-child(n+2):contains("+$('.invisible').text()+")").html(targetText).css("background-color", "#fbff9c").removeClass('draggable');

                    //add another slots
                    var courseID;
                    var hourStr;
                    $(".highlight").removeClass('highlight');
                    for (j = 0; j < countDrag; j++) {
                        courseID = target.attr('id').slice(0,3);
                        if (hour+j < 10) {
                            hourStr = "0"+(hour+j);
                        } else {
                            hourStr = "" +(hour+j);
                        }
                        $("#"+courseID+hourStr).draggable({disabled: false});
                        //add course + check its conflict
                        if ($("#"+courseID+hourStr).html() == "") {
                            $("#"+courseID+hourStr).html(dragText).css("background-color", drag.css("background-color")).addClass('draggable highlight');
                        } else {
                            if (typeof matconf[courseID+hourStr] === 'undefined') {
                                matconf[courseID+hourStr] = [];
                                matconf[courseID+hourStr].push($("#"+courseID+hourStr).html());
                                matconf[courseID+hourStr].push(dragText);

                                //alert conflict error
                                $("#"+courseID+hourStr).addClass("conflict");
                            } else {
                                matconf[courseID+hourStr].push(dragText);
                            }
                            //check if room clashes
                            found = false;
                            for (k = 0; k < matconf[courseID+hourStr].length-1; k++) {
                                for (l = k+1; l < matconf[courseID+hourStr].length; l++) {
                                    if (matconf[courseID+hourStr][k].substr(14) == matconf[courseID+hourStr][l].substr(14)) {
                                        $("#"+courseID+hourStr).addClass('conflict-room');
                                        found = true;
                                        break;
                                    }
                                }
                                if (found) break;
                            }
                        }
                    }

                    //update the currDomain day and startTime
                    for (j = 0; j < graph.variables.length; j++) {
                        if (dragText.search(graph.variables[j].courseName) !== -1) {
                            graph.variables[j].currDomain.startTime = hour;
                            graph.variables[j].currDomain.day = target.attr('id').substr(2,1);
                            break;
                        }
                    }

                    //Do ajax request!
                    $.post('/alter', JSON.stringify(graph), function(json) {
                        var data = JSON.parse(json);
                        $('#conflicts').text("Conflicts: " + data.conflicts);
                        $('#conflictsTS').text("Conflicts: " + data.conflictsTS);
                        $('#eff').text("Effectivity: " + data.eff + '%');
                        $('#effTS').text("Effectivity: " + data.effTS + '%');
                    });
                }
                else{
                    //if not empty, change duration
                    duration = 500;
                }
                $(this).addClass('draggable');
                $(ui.draggable).removeClass('draggable');
                $(".draggable").draggable({
                    revert: 'invalid',
                    snap: 'td:nth-child(n+2)',
                    helper: 'clone',
                    start: function( event, ui ) {
                        //make helper same height as td
                        $(ui.helper).css('height', $(this).height());
                        $(ui.helper).css('width', $(this).width());
                    }
                });
                //always have td revert
                ui.draggable.draggable('option', 'revertDuration', duration);
                ui.draggable.draggable('option', 'revert', true);
            }
        });
    });
});