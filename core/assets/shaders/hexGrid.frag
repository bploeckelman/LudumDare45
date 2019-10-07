#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform vec2 u_screenResolution;
uniform vec4 u_lineColor;
uniform vec4 u_leftColor;
uniform vec4 u_rightColor;
uniform vec4 explosions[200];
uniform vec3 explostionOwners[200];
uniform float u_hexScale;


varying vec4 v_color;
varying vec2 v_texCoords;

const float halfLineWidth = 2.;
const float gridSpacing = 20.;

#define PI 3.14
#define PI2 6.28

#define HX vec2(1., 0.)
#define HY vec2(.5, 0.866)
#define HMAT mat2(HX, HY)
#define HSCALE 40.

mat2 inverse(mat2 m) {
    return mat2(m[1][1],-m[0][1],
    -m[1][0], m[0][0]) / (m[0][0]*m[1][1] - m[0][1]*m[1][0]);
}

float pixelToCubial(vec2 uv, out vec3 cubial, out vec3 grid) {

    vec2 axial = inverse(HMAT) * uv;
    cubial = vec3(axial, -axial.x - axial.y) / (HSCALE * u_hexScale);
    grid = floor(.5 + cubial);
    vec3 dist = abs(cubial - grid);
    if (dist.x > dist.y && dist.x > dist.z) {
        grid.x = -grid.y - grid.z;
    }
    else if (dist.y > dist.z) {
        grid.y = -grid.x - grid.z;
    }
    else {
        grid.z = -grid.x - grid.y;
    }
    return 0.;
}

//// cubialDistance
//
//	finds the distance between 2 hex cell positions

float cubialDistance(vec3 a, vec3 b) {
    vec3 c = abs(a - b);
    return max(c.x, max(c.y, c.z));
}

void main(){
    vec2 coords = v_texCoords;
//    vec4 backgroundTex = texture2D(u_texture, coords);

    coords = coords * u_screenResolution;

//    vec4 colors = texture2D(u_texture, coords);
//    float x, y;
//    x = mod(coords.x, gridSpacing);
//    y = mod(coords.y, gridSpacing);
//    float pct = grid(vec2(x,y), gridSpacing /2.);
    vec3 cubial;
    vec3 grid;

    pixelToCubial(coords, cubial, grid);
    // get per tile coordinates

    vec3 coord = cubial - grid;
    vec3 dist = abs(coord.xyz - coord.zxy);

    vec4 background = mix(u_leftColor, u_rightColor, v_texCoords.x);
    float lineWidth = 1. - (.04 / u_hexScale);
    float lineMargin = .09 / u_hexScale;
    float pct = smoothstep(lineWidth - (.02/u_hexScale), lineWidth, max(dist.x, max(dist.y, dist.z)));
    float blackLine = smoothstep(lineWidth-lineMargin, lineWidth-(.03/u_hexScale), max(dist.x, max(dist.y, dist.z)));

//    vec4 finalColor = mix(backgroundTex, background, u_lineColor.a);
    vec4 finalColor = mix(vec4(0), background, u_lineColor.a);

    for (int i = 0; i < 40; i++){
        vec4 explosion = explosions[i];
        if (explosion.z > 0.){
            vec3 mouse;
            float mouseDist;
            pixelToCubial(explosion.xy, cubial, mouse);
            if ((mouseDist = cubialDistance(grid, mouse)) < (explosion.w / u_hexScale) * explosion.z) {
                vec3 ownerColor = explostionOwners[i];
                vec4 explosionColor = vec4(ownerColor,1.);
//                finalColor = mix(finalColor, explosionColor, .5 * explosion.z * ((explosion.w /u_hexScale) - 1. - floor(mouseDist)));
                finalColor = mix(finalColor, explosionColor, explosion.z *( 1. - (mouseDist / (.01 + explosion.z * (explosion.w /u_hexScale)))));

            }
        }
    }

//    finalColor.a = 1.;

//    vec4 background = mix(u_leftColor, u_rightColor, v_texCoords.x);
//    background = background * (1. + (intensity * 4.5));
    finalColor = mix(finalColor, vec4(vec3(0.), 1.), blackLine);
    gl_FragColor = mix(finalColor, background, pct);
//    gl_FragColor = vec4(pct);
}
