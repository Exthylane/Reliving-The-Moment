#version 150


uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float UTime;
uniform float UDelta;
uniform int UType;

out vec4 fragColor;


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
    vec2 f = texCoord * OutSize.xy;


    vec2 uv = (f - OutSize.xy * 0.5) / max(OutSize.x, OutSize.y);
    vec4 p  = vec4(uv.x, uv.y + IQ_TILT, 1., 0.);

    float time = UTime * IQ_SPEED;

    for (int i = 0; i < IQ_ITERS; i++)
    p *= IQ_STEP + IQ_SCALE * length(cos(IQ_CURVE * p.x + vec3(p.z + time, p.xy)))
    + IQ_FINE * cos(4. * p.y);

    p = (p + p.z) * IQ_SCALE;

    // color grade
    vec3 col = p.rgb * IQ_COLOR;
    col = pow(max(col, 0.0), vec3(0.8));

    return vec4(col, 1.0);
}


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
    vec2 uv = texCoord * OutSize.xy / max(OutSize.x, OutSize.y);

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

void main() {
    vec4 scene = texture(DiffuseSampler, texCoord);

    if (UDelta <= 0.0) {
        fragColor = scene;
        return;
    }


    vec2 st = texCoord - 0.5;
    float dist = length(st) * 1.42;

    float strenght = UDelta*0.5;

    float edgeMask = smoothstep(1.0 - strenght, 1.0 - strenght + 0.3, dist);

    vec4 effect = (UType == 0) ? iqMarch() : starNest();

    fragColor = vec4(mix(scene.rgb, effect.rgb, edgeMask), 1.);
}