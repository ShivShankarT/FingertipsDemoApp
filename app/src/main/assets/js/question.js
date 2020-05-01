function ready(fn) {
    var d = document;
    (d.readyState == 'loading') ? d.addEventListener('DOMContentLoaded', fn) : fn();
}

function removeClass(el, className) {
    if (el.classList)
        el.classList.remove(className);
    else if (hasClass(el, className)) {
        var reg = new RegExp('(\\s|^)' + className + '(\\s|$)');
        el.className = el.className.replace(reg, ' ');
    }
}

ready(function () {
    configureMathjax();
    if (!('jsObject' in window))
        displayMockQuestions();
    console.log(":done")
});


function getOptionContainerId(option) {
    var optionContId;
    switch (option) {
        case 'A':
            optionContId = "container_option_a";
            break;
        case 'B':
            optionContId = "container_option_b";
            break;
        case 'C':
            optionContId = "container_option_c";
            break;
        case 'D':
            optionContId = "container_option_d";
            break;

    }
    return optionContId;
}

var  selectedOption=null;

function optionClicked(option) {
    console.log(":done" + option +" "+selectedOption);



    if (selectedOption!==null){
        var optionContId = getOptionContainerId(selectedOption);
        var optionElement = document.getElementById(optionContId);
        if (optionElement.classList.contains("selected"))
            optionElement.classList.remove("selected");

    }

    var optionContId = getOptionContainerId(option);
    var optionElement = document.getElementById(optionContId);
    if (optionElement.classList.contains("selected"))
        optionElement.classList.remove("selected");
    else
       optionElement.classList.add("selected");

    selectedOption=option;

    if ('jsObject' in window)
        jsObject.onOptionClicked(selectedOption);

}

function configureMathjax() {
    MathJax.Hub.Config({
        showProcessingMessages: false,
        showMathMenu: false,
        messageStyle: "none",
        tex2jax: {
            inlineMath: [['$$', '$$'], ['\\(', '\\)']],
            displayMath: [['$$$', '$$$'], ['\\(', '\\)']]
        },
        styles: {
            ".MathJax_SVG svg > g, .MathJax_SVG_Display svg > g": {
                fill: "#4A4A4A",
                stroke: "#4A4A4A"
            }
        },
        SVG: {
            linebreaks: {
                automatic: true,
                width: "container"
            },
            minScaleAdjust: 50

        }
    });

    MathJax.Hub.Register.StartupHook('End', function () {
        if ('jsObject' in window)
            jsObject.mathjax_done();
    });
}


function displayMockQuestions() {

    var questions = {
        question: "Temperature at the $$x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}.$$ foot of a mountain is $$+5^{\\circ}$$C. It fell down by $$10^{\\circ}$$C at the top of the mountain. The temperature <span>recorded on the top is</span>",
        question_image: "https://via.placeholder.com/150",
        questionExp: "sss",
        questionExpImage: "https://via.placeholder.com/150",
        selectedOption: "A",
        answer: "D",
        options: [
            {
                option: "A thin uniform rod, pivoted at $$O$$, is rotating in the horizontal plane with constant angular speed $$\omega$$, as shown in the figure. At time $$t = 0$$, a small insect starts from $$O$$ and moves with constant speed $$v$$ with respect to the rod towards the other end.",
                optionType: "TEXT"//TEXT or IMAGE
            }
            ,
            {
                option: "https://via.placeholder.com/550x100",
                optionType: "IMAGE"//TEXT or IMAGE
            },

            {
                option: "CCCC",
                optionType: "TEXT"//TEXT or IMAGE
            },
            {
                option: "DDDD",
                optionType: "TEXT"//TEXT or IMAGE
            }

        ]

    };

    var questionsJSONEncoded = window.btoa(JSON.stringify(questions));
    displayQuestions(questionsJSONEncoded);
}


function displayQuestions(questionsJSONEncoded) {

    try {
        var question = decodedBase64ToJSONObject(questionsJSONEncoded);

        document.getElementById('question').innerHTML = question.question;
        if (question.question_image !== undefined && question.question_image !== "") {
            document.getElementById('question_image').src = question.question_image;

        } else {
            document.getElementById('question_image').remove();
        }

        for (var i = 0; i < question.options.length; i++) {
            var opt = question.options[i].option;
            var optionType = question.options[i].optionType;
            var optionElementId = '';
            var optionContId = '';
            var optionImgElementId = '';
            var optionLabel = '';

            switch (i) {
                case 0:
                    optionElementId = 'opt_a';
                    optionImgElementId = 'opt_a_image';
                    optionContId = "container_option_a";
                    optionLabel = "A";
                    break;
                case 1:
                    optionElementId = 'opt_b';
                    optionImgElementId = 'opt_b_image';
                    optionContId = "container_option_b";
                    optionLabel = "B";
                    break;
                case 2:
                    optionElementId = 'opt_c';
                    optionImgElementId = 'opt_c_image';
                    optionContId = "container_option_c";
                    optionLabel = "C";
                    break;
                case 3:
                    optionElementId = 'opt_d';
                    optionImgElementId = 'opt_d_image';
                    optionContId = "container_option_d";
                    optionLabel = "D";
                    break;

            }


            if (optionLabel === question.selectedOption) {
                var optionElement = document.getElementById(optionContId);
                optionElement.classList.add("selected");

            }



            if (optionLabel === question.answer) {
                var optionElement = document.getElementById(optionContId);
                optionElement.classList.add("correct");

            }


            var optionElement = document.getElementById(optionElementId);
            var optionImgElement = document.getElementById(optionImgElementId);
            if (optionType === "TEXT") {
                optionElement.innerHTML = opt;
                optionImgElement.remove();
            } else {
                optionElement.remove();
                optionImgElement.src = opt;
            }
        }

        document.getElementById('explanation_question').innerHTML = question.questionExp;


        if (question.questionExpImage !== undefined && question.questionExpImage !== "") {
            document.getElementById('explanation_question_img').src = question.questionExpImage;

        } else {
            document.getElementById('explanation_question_img').remove();
        }

        if (question.questionExp !== undefined && question.questionExp !== "") {

        } else {
            if (question.questionExpImage !== undefined && question.questionExpImage !== "") {
            } else {
                document.getElementById('explanation_ly').remove();
            }
        }


        setTimeout(function () {
            MathJax.Hub.Queue(
                ['Typeset', MathJax.Hub],
                function () {
                    if ('jsObject' in window) jsObject.onQuestionLoad();
                }
            );
        }, 0);

        //.removeClass("hidden");

        removeClass(document.getElementById("question_container"), "hidden")


    } catch (e) {
        console.error('Failed to parse question JSON', e);
        if ('jsObject' in window)
            jsObject.onQuestionError();
    }
}

function decodedBase64ToJSONObject(base64EncodedString) {
    try {
        var jsonString;
        if ('jsObject' in window) {
            jsonString = b64DecodeUnicode(base64EncodedString);
        } else
            jsonString = window.atob(base64EncodedString);


        console.log(jsonString);
        //return $.parseJSON(jsonString);
        //var actual = JSON.parse(atob(encoded))  var encoded = btoa(JSON.stringify(obj))
        return JSON.parse(jsonString);
    } catch (e) {
        console.error('Error while decoding to json object', e);
    }
}

function b64DecodeUnicode(str) {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(window.atob(str).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}
