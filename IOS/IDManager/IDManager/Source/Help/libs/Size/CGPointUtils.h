/*
 *  CGPointUtils.h
 *  PinchMe
 *
 *  Created by Jeff LaMarche on 8/2/08.
 *  Copyright 2008 __MyCompanyName__. All rights reserved.
 *
 */
#import <CoreGraphics/CoreGraphics.h>
#import <math.h>
#define pi 3.14159265358979323846
#define degreesToRadian(x) (pi * x / 180.0)
#define radiansToDegrees(x) (180.0 * x / pi)

CGFloat distanceBetweenPoints (CGPoint first, CGPoint second);
CGFloat angleBetweenPoints(CGPoint first, CGPoint second);
CGFloat angleBetweenLines(CGPoint line1Start, CGPoint line1End, CGPoint line2Start, CGPoint lin2End);
#define cgp(x,y) ((CGPoint){(x),(y)})
static inline CGPoint cgpMult(CGPoint v, float s)
{
    return cgp(v.x *s, v.y*s);
}

static inline CGPoint cgpNormalize(CGPoint v)
{
    return cgpMult(v, 1.0f/sqrtf((float)(v.x*v.x + v.y*v.y)));
}
static inline CGPoint cgpSub(CGPoint a, CGPoint b){
    return cgp(a.x-b.x, a.y - b.y);
}
static inline float cgpDot(CGPoint a, CGPoint b){
    return a.x*b.x + a.y *b.y;
}
static inline CGFloat angleSignBetweenLines(CGPoint line1Start, CGPoint line1End, CGPoint line2Start, CGPoint line2End){
    CGPoint a = cgpSub(line1End, line1Start);
    CGPoint b = cgpSub(line2End, line2Start);
    CGPoint a2 = cgpNormalize(a);
    CGPoint b2 = cgpNormalize(b);
    float angle = atan2f(a2.x * b2.y - a2.y * b2.x, cgpDot(a2, b2));
    if( fabsf(angle) < FLT_EPSILON ){
        angle = 0.0f;
    }
    angle = radiansToDegrees(angle);
    return angle;
}