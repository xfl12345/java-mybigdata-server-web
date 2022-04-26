const copy2Object = (src, dest) => {
    const srcKeys = Object.keys(src);
    const destKeys = Object.keys(dest);
    if (srcKeys.length >= destKeys.length) {
        Object.keys(dest).forEach((key) => {
            if (srcKeys.includes(key)) {
                dest[key] = src[key];
            }
        });
    } else {
        Object.keys(src).forEach((key) => {
            if (destKeys.includes(key)) {
                src[key] = dest[key];
            }
        });
    }
};
export {copy2Object};
