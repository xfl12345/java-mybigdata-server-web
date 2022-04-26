class ElementPlusAlertType {
    success;

    info;

    warning;

    error;

    constructor() {
        Object.keys(this).forEach((key) => {
            this[key] = String(key);
        });
    }
}

export {ElementPlusAlertType};
