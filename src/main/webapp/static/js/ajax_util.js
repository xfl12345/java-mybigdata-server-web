import AjaxManual from "./AjaxManual.js";

function ajax(options) {
    const ajaxManual = new AjaxManual(options);
    return ajaxManual.sendRequest();
}

export default ajax;
