//
//  TDSize.h
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface TDSize : NSObject

@end

static inline CGRect TDRectFromSize(CGSize size,float scale){
    return CGRectMake(0, 0, size.width *scale, size.height*scale);
}
static inline CGRect TDRectFromSizeScaleXY(CGSize size,float scaleX, float scaleY){
    return CGRectMake(0, 0, size.width *scaleX, size.height*scaleY);
}

static inline CGPoint TDPointFromSize(CGSize size, float scale){
    return CGPointMake(size.width *scale, size.height*scale);
}

static inline CGPoint TDRealCenter(UIView *v){
    CGPoint p = TDPointFromSize(v.frame.size, 0.5f);
    return [v.superview convertPoint:p fromView:v];
}