// postcss.config.cjs
module.exports = {
    plugins: {
        'postcss-px-to-viewport': {
            viewportWidth: 375,  // 设计稿的宽度
            viewportHeight: 667, // 设计稿的高度
            unitPrecision: 3,    // 转换后的精度，保留小数点后几位
            viewportUnit: 'vw',  // 转换成的视窗单位，可选 vw、vh、vmin、vmax
            selectorBlackList: ['.ignore'], // 不进行转换的css类名
            minPixelValue: 1,    // 最小转换单位，低于1px的值不转换
            mediaQuery: false    // 允许在媒体查询中转换px
        },
    },
};
