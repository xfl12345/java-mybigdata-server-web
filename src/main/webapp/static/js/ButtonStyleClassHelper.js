class StateBook {
    normal;

    succeed;

    failed;

    doing;
}

class ButtonStyleClassHelper {
    alsoDisplayStateText;

    alsoSetDisableAttribute;

    elementId;

    stateBook;

    stateDisplayText;

    currentState;

    currentTemporaryStateHandler;

    constructor(
        elementId,
        {styleBook = null, alsoDisplayStateText = true, alsoSetDisableAttribute = true}
    ) {
        // 为避免this混用，创建个临时变量 指向 当前对象
        const myself = this;

        // 设置状态的同时，是否顺便切换按钮显示的文字
        this.alsoDisplayStateText = alsoDisplayStateText;
        // 设置状态的同时，是否顺便设置按钮禁用状态
        this.alsoSetDisableAttribute = alsoSetDisableAttribute;

        this.elementId = elementId;

        this.stateBook = new StateBook();
        Object.keys(this.stateBook).forEach((key) => {
            myself.stateBook[key] = key;
        });

        this.styleBook = new StateBook();
        this.styleBook.normal = "btn-outline-primary";
        this.styleBook.succeed = "btn-success";
        this.styleBook.failed = "btn-danger";
        this.styleBook.doing = "btn-secondary";
        if (styleBook !== null) {
            Object.keys(this.stateBook).forEach((key) => {
                if (key in styleBook) {
                    const theStyleName = styleBook[key];
                    if (theStyleName instanceof String) {
                        myself.styleBook[key] = theStyleName;
                    }
                }
            });
        }

        this.stateDisplayText = new StateBook();
        this.stateDisplayText.succeed = "操作成功";
        this.stateDisplayText.failed = "操作失败";
        this.stateDisplayText.doing = "正在执行";
        this.stateDisplayText.normal = this.getElement().innerHTML;

        this.currentState = this.stateBook.normal;
        this.currentTemporaryStateHandler = null;

        // 设置状态的通用函数（内部私有，只在初始化对象时可用）
        const commonSetState = function (stateKey) {
            myself.tryAndSkipError(() => {
                clearTimeout(myself.currentTemporaryStateHandler);
            });
            myself.removeAllState();
            myself.currentState = stateKey;
            if (myself.alsoSetDisableAttribute) {
                myself.getElement().disabled = stateKey === myself.stateBook.doing;
            }
            myself.tryAndSkipError(() => {
                myself.getElement().classList.add(myself.styleBook[stateKey]);
                if (myself.alsoDisplayStateText) {
                    myself.getElement().innerHTML = myself.stateDisplayText[stateKey];
                }
                // console.log("set " + getTimeHHmmss());
            });
        };

        // 设置临时状态的类（内部私有，只在初始化对象时可用）
        class CommonSetTemporaryStateObject {
            constructor(stateKey) {
                this.setTempState = function (turn2normal, timeOut) {
                    timeOut = timeOut instanceof Number ? timeOut : 3000;
                    let backupCurrentState;
                    if (turn2normal) {
                        backupCurrentState = myself.stateBook.normal;
                    } else {
                        backupCurrentState = myself.currentState;
                    }
                    myself.setState[stateKey]();
                    myself.currentTemporaryStateHandler = setTimeout(() => {
                        myself.setState[backupCurrentState]();
                        // console.log("clear " + getTimeHHmmss());
                    }, timeOut);
                };
            }
        }

        const thePack = {commonSetState, CommonSetTemporaryStateObject};

        // 设置状态的函数集合
        // 这里对内部详细定义是为了让笨笨的IDE支持智能补全
        this.setState = {
            normal: () => {
            },
            succeed: () => {
            },
            failed: () => {
            },
            doing: () => {
            }
        };
        this.setTemporaryState = {
            normal: (turn2normal, timeOut) => {
            },
            succeed: (turn2normal, timeOut) => {
            },
            failed: (turn2normal, timeOut) => {
            },
            doing: (turn2normal, timeOut) => {
            }
        };
        Object.keys(this.styleBook).forEach((key) => {
            const values = myself.stateBook[key];
            myself.setState[key] = () => thePack.commonSetState(key);
            const setTemporaryStateObject = new thePack.CommonSetTemporaryStateObject(key);
            myself.setTemporaryState[key] = (turn2normal, timeOut) => {
                setTemporaryStateObject.setTempState(turn2normal, timeOut);
            };
        });
    }

    getElement = () => {
        return document.getElementById(this.elementId);
    };

    static tryAndSkipError = (func) => {
        try {
            func();
            // eslint-disable-next-line no-empty
        } catch (e) {
        }
    };

    removeAllState = () => {
        const myself = this;
        Object.keys(this.styleBook).forEach((stateKey) => {
            myself.tryAndSkipError(() => {
                myself.getElement().classList.remove(myself.styleBook[stateKey]);
            });
        });
    };
}

export {ButtonStyleClassHelper, StateBook};
