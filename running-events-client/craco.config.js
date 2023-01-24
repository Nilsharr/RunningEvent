module.exports = {
    babel: {
        loaderOptions: {
            ignore: ['./node_modules/mapbox-gl/dist/mapbox-gl.js'],
        },
    },
    devServer: {
        port: 4100
    }
}