console.log(graph);

//color array
var colorarr = ["#99ccff", "#cc66ff", "#ff99cc", "#ff9966", "#ffd11a", "#99ff33", "#66ff66", "#00ff99", "#009999",
    "#0099cc", "#cc6699", "#a31aff", "#cccc00", "#73e600", "#00ffff", "#ffcc99", "#cc00cc", "#009999"];
/* var colorarr = []
 for (var i = 0; i < 18; i++) {
 colorarr.push(`hsl(${(i/18)}, 1, 0.5)`);
 } */
var mapcolor = {};

//Assign slot
$(document).ready(function () {
    //variables
    var slot;
    var sss;
    var len = graph.variables.length;
    var dur;
    var x;
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
                $("#"+sss).html("<div>"+graph.variables[i].courseName+"</div>");
            } else {
                //check for matconf obj
                if (typeof matconf[sss] === 'undefined') {
                    matconf[sss] = [];
                    matconf[sss].push($("#"+sss).html());
                    matconf[sss].push("<div>"+graph.variables[i].courseName+"</div>");

                    //alert conflict error
                    $("#"+sss).addClass("conflict");
                } else {
                    matconf[sss].push("<div>"+graph.variables[i].courseName+"</div>");
                }
            }
            $("#"+sss).addClass("draggable");
        }
    }

    console.log(matconf);
    for (i = 0; i < graph.variables.length; i++) {
        mapcolor["<div>"+graph.variables[i].courseName+"</div>"] = colorarr[i];
        $("td:contains("+graph.variables[i].courseName+")").css("background-color", colorarr[i]);
    }

    /* for (var xz in matconf) {
        let i = xz;
        $("#"+i+" > div").dblclick(function () {
            pushSwitch(matconf[i]);
            $(this).html(matconf[i][0]);
            for (var ab in matconf) {
                let j = ab;
                pushCourse(matconf[i][0],matconf[j],j);
            }
        });
    } */

    $(document).on("dblclick", "td:nth-child(n+2) > div", function () {
        var courseShow = "<div>"+$(this).text()+"</div>";
        if ($(this).parent().hasClass("conflict")) {
            var idConf = $(this).parent().attr('id');
            pushSwitch(matconf[idConf]);
            courseShow = matconf[idConf][0];
        }
        for (var ab in matconf) {
            let j = ab;
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

    $(".draggable").draggable( {
        containment: 'document',
        snap: 'td:nth-child(n+2)'
    });
});