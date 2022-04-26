import isUndefinedOrNull from "./isUndefinedOrNull.js";

class AjaxManual {
    xhr;

    params;

    options;

    constructor(options) {
        // 调用函数时如果options没有指定，就给它赋值{},一个空的Object
        if (isUndefinedOrNull(options)) options = {};

        // 请求附带的可选参数（先声明一个符号）
        this.params = null;
        // 如果请求数据格式为空，则默认为 application/json
        if (isUndefinedOrNull(options.contentType)) {
            if (!isUndefinedOrNull(options.data) && typeof options.data !== "string") {
                options.contentType = "application/json;charset=utf-8";
                this.params = JSON.stringify(options.data); // 默认请求数据格式是JSON
            } else {
                options.contentType = "text/plain;charset=utf-8";
                this.params = String(options.data);
            }
        } else if (options.contentType.indexOf("application/json") !== -1) {
            if (!isUndefinedOrNull(options.data) && typeof options.data !== "string") {
                this.params = JSON.stringify(options.data);
            }
        } else if (options.contentType.indexOf("application/x-www-form-urlencoded") !== -1) {
            this.params = this.formatParams(options.data);
        }

        if (isUndefinedOrNull(this.params)) {
            this.params = options.data;
        }

        // 考虑兼容性
        if (window.XMLHttpRequest) {
            this.xhr = new XMLHttpRequest();
        } else if (window.ActiveObject) {
            // 兼容IE6以下版本
            this.xhr = new window.ActiveXobject("Microsoft.XMLHTTP");
        }

        // 设置有效时间
        setTimeout(() => {
            if (this.xhr.readySate !== 4) {
                this.xhr.abort();
            }
        }, options.timeout);

        this.xhr.ontimeout = (event) => {
            if (isUndefinedOrNull(options.ontimeout)) {
                options.ontimeout(event);
            }
        };

        // 请求格式GET、POST，默认为POST
        if (isUndefinedOrNull(options.type)) {
            options.type = "POST";
        }

        options.type = options.type.toUpperCase();
        this.options = options;
    }

    // 格式化请求参数
    // eslint-disable-next-line class-methods-use-this
    formatParams = (data) => {
        const arr = [];
        Object.keys(data).forEach((key) => {
            arr.push(encodeURIComponent(key) + "=" + encodeURIComponent(data[key]));
        });
        arr.push(("v=" + Math.random()).replace(".", ""));
        return arr.join("&");
    };

    // 可以自定义任何时间，手动发送HTTP请求，但不阻塞
    sendRequest = () => {
        const myself = this;
        // 启动并发送一个请求
        if (this.options.type === "GET") {
            if (!isUndefinedOrNull(this.params)) {
                myself.xhr.open("get", this.options.url + "?" + this.params);
            } else {
                myself.xhr.open("get", this.options.url);
            }
            myself.xhr.send(null);
        } else if (this.options.type === "POST") {
            myself.xhr.open("post", this.options.url);
            // 设置表单提交时的内容类型
            // Content-type数据请求的格式
            myself.xhr.setRequestHeader("Content-type", this.options.contentType);
            if (!isUndefinedOrNull(this.params)) {
                myself.xhr.send(this.params);
            } else {
                myself.xhr.send();
            }
        } else {
            myself.xhr.open(this.options.type, this.options.url);
            myself.xhr.setRequestHeader("Content-type", this.options.contentType);
            if (!isUndefinedOrNull(this.params)) {
                myself.xhr.send(this.params);
            } else {
                myself.xhr.send();
            }
        }

        // options.success成功之后的回调函数  options.error失败后的回调函数
        // xhr.responseText,xhr.responseXML  获得字符串形式的响应数据或者XML形式的响应数据
        // 接收 HTTP 响应
        // 使用 then 关键词来确保顺序执行
        return new Promise((resolve, reject) => {
            myself.xhr.onreadystatechange = () => {
                if (myself.xhr.readyState === 4) {
                    const status = myself.xhr.status;
                    if ((status >= 200 && status < 300) || status === 304) {
                        resolve(myself.xhr.responseText, myself.xhr.getResponseHeader("Content-Type"));
                    } else {
                        reject(status);
                    }
                }
            };
        });
    };
}

export default AjaxManual;
