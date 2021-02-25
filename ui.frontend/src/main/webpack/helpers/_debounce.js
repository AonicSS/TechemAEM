export const debounce = (fn, time) => {
    let timeout;

    return function(...rest) {
        const functionCall = () => fn.apply(this, rest);

        clearTimeout(timeout);
        timeout = setTimeout(functionCall, time);
    };
};
