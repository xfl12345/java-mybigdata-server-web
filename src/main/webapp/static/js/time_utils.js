// source code URL=https://blog.csdn.net/yuzheh521/article/details/106016981
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18

// eslint-disable-next-line no-extend-native
class ProDate extends Date {
    constructor() {
        super();
        this.proFormat = (fmt) => {
            const o = {
                "M+": this.getMonth() + 1, // 月份
                "d+": this.getDate(), // 日
                "H+": this.getHours(), // 小时
                "m+": this.getMinutes(), // 分
                "s+": this.getSeconds(), // 秒
                "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
                S: this.getMilliseconds() // 毫秒
            };
            if (/(y+)/.test(fmt)) {
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            // eslint-disable-next-line no-restricted-syntax
            for (const k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(
                        RegExp.$1,
                        RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)
                    );
                }
            }
            return fmt;
        };
    }
}

const getTimeYYYYMMDD = () => {
    return new ProDate().proFormat("yyyy-MM-dd");
};

const getTimeHHmmss = () => {
    return new ProDate().proFormat("HH:mm:ss");
};

const getTimeYYYYMMDDHHmmss = () => {
    return new ProDate().proFormat("yyyy-MM-dd HH:mm:ss");
};

const getTimeYYYYMMDDHHmmssSSS = () => {
    return new ProDate().proFormat("yyyy-MM-dd HH:mm:ss S");
};

const getTimeSSS = () => {
    return new ProDate().proFormat("S");
};

const getTimeInFormat = (format) => {
    return new ProDate().proFormat(format);
};

// 例子：
// var date1 = new ProDate().Format("yyyy-MM-dd");
// var date2 = new ProDate().Format("yyyy-MM-dd HH:mm:ss");
// var date3 = new ProDate().Format("qq ");
// console.log(date1)
// console.log(date2)
// console.log(date3)

export {ProDate, getTimeInFormat, getTimeYYYYMMDD, getTimeHHmmss, getTimeYYYYMMDDHHmmssSSS};
