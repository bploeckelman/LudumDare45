#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_time;

//input from vertex shader
varying vec4 v_color;
varying vec2 v_texCoords;


float hash( float x )
{
    return fract( sin( x ) * 43758.5453 );
}

float noise( vec2 uv )  // Thanks Inigo Quilez
{
    vec3 x = vec3( uv.xy, 0.0 );

    vec3 p = floor( x );
    vec3 f = fract( x );

    f = f*f*(3.0 - 2.0*f);

    float offset = 57.0;

    float n = dot( p, vec3(1.0, offset, offset*2.0) );

    return mix( mix(    mix( hash( n + 0.0 ),       hash( n + 1.0 ), f.x ),
    mix( hash( n + offset),     hash( n + offset+1.0), f.x ), f.y ),
    mix(    mix( hash( n + offset*2.0), hash( n + offset*2.0+1.0), f.x),
    mix( hash( n + offset*3.0), hash( n + offset*3.0+1.0), f.x), f.y), f.z);
}

float snoise( vec2 uv )
{
    return noise( uv ) * 2.0 - 1.0;
}


float perlinNoise( vec2 uv )
{
    float n =       noise( uv * 1.0 )   * 128.0 +
    noise( uv * 2.0 )   * 64.0 +
    noise( uv * 4.0 )   * 32.0 +
    noise( uv * 8.0 )   * 16.0 +
    noise( uv * 16.0 )  * 8.0 +
    noise( uv * 32.0 )  * 4.0 +
    noise( uv * 64.0 )  * 2.0 +
    noise( uv * 128.0 ) * 1.0;

    float noiseVal = n / ( 1.0 + 2.0 + 4.0 + 8.0 + 16.0 + 32.0 + 64.0 + 128.0 );
    noiseVal = abs(noiseVal * 2.0 - 1.0);

    return  noiseVal;
}

float cubicPulse( float c, float w, float x )
{
    x = abs(x - c);
    if( x>w ) return 0.0;
    x /= w;
    return 1.0 - x*x*(3.0-2.0*x);
}


void main() {
    vec4 texSample = texture2D(u_texture, v_texCoords);
    float n1 = abs(perlinNoise(vec2(v_texCoords.x * 100. + u_time*2., v_texCoords.y + u_time/2.5))) * .6;
    float n2 = abs(perlinNoise(vec2(v_texCoords.x * 100. - u_time*3., v_texCoords.y + u_time/2.))) * .3;
    float n3 = abs(perlinNoise(vec2(v_texCoords.x * 100. - u_time*5., v_texCoords.y + u_time))) * .2;

    float crackle = abs(pow(perlinNoise(vec2(v_texCoords.x * 10000. - u_time*1., v_texCoords.y * 100. + u_time*6.)), 4.)) * 100.;

    float lineEdge = .04;
    float centerLine = cubicPulse(.5, lineEdge, v_texCoords.y);
    vec4 lineColor = v_color;
//    lineColor.rgb = mix(v_color.rgb, vec3(.7), centerLine);
    // NOTE: this adds in the noise layer
//    vec4 finalColor = texSample * lineColor + vec4(vec3(n1+n2+n3)*.6, 0);
    vec4 finalColor = texSample * lineColor;

    // NOTE: this adds in the 'crackle' effect
//    finalColor.a = max(crackle * finalColor.a, finalColor.a);

    gl_FragColor = finalColor;
}