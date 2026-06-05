#version 150

uniform sampler2D Sampler0;


uniform float UTime;
uniform int UType;
uniform vec2 UScreenSize;

in vec2 texCoord0;

out vec4 fragColor;


// Based on "Star Nest" by Pablo Roman Andrioli (Kali)
// Original shader licensed under MIT
// Modified for Minecraft rendering
// https://www.shadertoy.com/view/XlfGRj

#define iterations 17
#define formuparam 0.53

#define volsteps 20
#define stepsize 0.1

#define zoom   1.800
#define tile   0.850
#define speed  0.001

#define brightness 0.0015
#define darkmatter 0.300
#define distfading 0.730
#define saturation 1.250


vec4 starNest() {
    vec2 uv = (texCoord0 - 0.5) * UScreenSize / max(UScreenSize.x, UScreenSize.y);

    vec3 dir=vec3(uv*zoom,1.);
    float time=UTime*speed+.25;


    vec3 from=vec3(1.,.5,0.5);
    from+=vec3(time*2.,time,-2.);


    float s=0.1,fade=1.;
    vec3 v=vec3(0.);
    for (int r=0; r<volsteps; r++) {

        vec3 p=from+s*dir*.5;
        p = abs(vec3(tile)-mod(p,vec3(tile*2.)));
        float pa,a=pa=0.;

    for (int i=0; i<iterations; i++) {
    p=abs(p)/dot(p,p)-formuparam;
    a+=abs(length(p)-pa);
    pa=length(p);
    }
    float dm=max(0.,darkmatter-a*a*.001);
    a*=a*a;
    if (r>6) fade*=1.-dm;
    v+=fade;
    v+=vec3(s,s*s,s*s*s*s)*a*brightness*fade;
    fade*=distfading;
    s+=stepsize;
    }
    v=mix(vec3(length(v)),v,saturation);

    return vec4(v*.01,.75);
}

// The MIT License
// Copyright © 2023 Inigo Quilez
// https://iquilezles.org/
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions: The above copyright
// notice and this permission notice shall be included in all copies or
// substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS",
// WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED.

#define IQ_ITERS  80
#define IQ_SCALE  0.1
#define IQ_CURVE  0.7
#define IQ_STEP   0.9
#define IQ_FINE   0.01
#define IQ_SPEED  0.4
#define IQ_TILT   0.0
#define IQ_COLOR  vec3(1., 0., 0.2)

vec4 iqMarch() {
    vec2 f = texCoord0 * UScreenSize.xy;

    vec2 uv = (f - UScreenSize.xy * 0.5) / max(UScreenSize.x, UScreenSize.y);
    vec4 p  = vec4(uv.x, uv.y + IQ_TILT, 1., 0.);

    float time = UTime * IQ_SPEED;

    for (int i = 0; i < IQ_ITERS; i++)
    p *= IQ_STEP + IQ_SCALE * length(cos(IQ_CURVE * p.x + vec3(p.z + time, p.xy)))
    + IQ_FINE * cos(4. * p.y);

    p = (p + p.z) * IQ_SCALE;

    vec3 col = p.rgb * IQ_COLOR;
    col = pow(max(col, 0.0), vec3(0.8));

    return vec4(col, 1.0);
}

float pw_hash(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453);
}

vec2 pw_hash2(vec2 p) {
    return fract(sin(mat2(127.1,311.7,269.5,183.3) * p) * 43758.5453);
}


float perlin(vec2 p) {
    vec2 ip = floor(p), fp = fract(p);
    vec2 u  = fp*fp*(3.0-2.0*fp);
    float a = pw_hash(ip);
    float b = pw_hash(ip + vec2(1,0));
    float c = pw_hash(ip + vec2(0,1));
    float d = pw_hash(ip + vec2(1,1));
    return mix(mix(a,b,u.x), mix(c,d,u.x), u.y);
}


float worley(vec2 p) {
    vec2 ip = floor(p), fp = fract(p);
    float md = 8.0;
    for (int y = -1; y <= 1; y++)
    for (int x = -1; x <= 1; x++) {
        vec2 nb  = vec2(x, y);
        vec2 pt  = pw_hash2(ip + nb) + nb;
        md = min(md, length(pt - fp));
    }
    return 1.0 - md;
}


float worleyFbm(vec2 p) {
    return worley(p)      * 0.625
    + worley(p*2.0)  * 0.250
    + worley(p*4.0)  * 0.125;
}


float remap(float v, float lo, float hi, float no, float ni) {
    return no + (v - lo) * (ni - no) / max(hi - lo, 1e-5);
}


float perlinWorley(vec2 p) {
    float pe = perlin(p);
    float wf = worleyFbm(p);
    float pw = remap(pe, wf - 1.0, 1.0, 0.0, 1.0);
    return clamp(pw, 0.0, 1.0);
}

float gluNoise(vec2 p, float t) {
    vec2 drift1 = p + vec2(t * 0.04, t * 0.02);
    vec2 drift2 = p - vec2(t * 0.03, t * 0.05);

    float pw1 = perlinWorley(drift1 * 2.5);
    float pw2 = perlinWorley(drift2 * 2.5 + pw1 * 0.6);

    return pw1 * 0.5 + pw2 * 0.5;
}



void main() {
    vec4 v = vec4(0.);
    if (UType == 0) {
        v = iqMarch();
    } else {
        v = starNest();
    }

        vec2 uv2 =  texCoord0.xy;

        vec2 edge = min(uv2, 1.0 - uv2);

        float ex = smoothstep(0.0, 0.45, edge.x);
        float ey = smoothstep(0.0, 0.45, edge.y);
        float edgeFactor = ex * ey;

    float ex2 = smoothstep(0.0, 0.05, edge.x);
    float ey2 = smoothstep(0.0, 0.05, edge.y);
    float edgeFactor2 = ex2 * ey2;

    float noise = gluNoise(uv2 * 2.0, UTime);
    vec4 eqss = vec4(0.75,0.1,0.2,.75);

    fragColor = vec4(((v.rgb)*edgeFactor)+((eqss.rgb*noise)*edgeFactor2),(.75*edgeFactor)+((.25*noise)*edgeFactor2));
}





