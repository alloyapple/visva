//
//  ASPBusyCircleView.m
//  Groupon
//
//  Created by AnhVT on 12/8/10.
//  Copyright 2010 AI&T. All rights reserved.
//

#import "ASPBusyCircleView.h"
#import <QuartzCore/QuartzCore.h>
#import "ASPurchaseView.h"
#import "ASPGlobal.h"

@implementation ASPBusyCircleView
@synthesize parent;
@synthesize isRetain;
@synthesize busyView;
@synthesize contentLabel;
@synthesize retainCount;

- (id)init:(BOOL)setRetain {
    self.isRetain = setRetain;
	CGRect rect = CGRectMake(0, 0, 120, 50);
    retainCount = 0;
    if ((self = [super initWithFrame:rect])) {
		self.backgroundColor = [UIColor colorWithWhite:0.2 alpha:0.9];
        CGRect mainRect = [[UIScreen mainScreen] bounds];
        ASLOC(@"MainRect = (%f,%f,%f,%f)",mainRect.origin.x, mainRect.origin.y, mainRect.size.width,
              mainRect.size.height);
		self.center = CGPointMake(mainRect.size.width / 2, mainRect.size.height / 2);
		
        self.clipsToBounds = YES;
		self.layer.cornerRadius = 8.0;
		
		busyView = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle: UIActivityIndicatorViewStyleWhite];
		busyView.hidesWhenStopped = YES;
        busyView.frame = CGRectMake(0, 0, 22, 22);
		busyView.center = CGPointMake(self.bounds.size.width / 2, self.bounds.size.height / 2 - 8);
		[self addSubview:busyView];
		
        
		contentLabel = [[UILabel alloc] initWithFrame: CGRectMake(0, 30, self.bounds.size.width, 17)];
		contentLabel.text = @"";
		contentLabel.backgroundColor = [UIColor clearColor];
		contentLabel.textColor = [UIColor whiteColor];
		contentLabel.textAlignment = UITextAlignmentCenter;
		contentLabel.font = [UIFont boldSystemFontOfSize: 15];
		[self addSubview:contentLabel];
        
    }
    return self;
}

- (void)startWithTitle:(NSString*)title {
    if (isRetain) {
        retainCount++;
        ASLOC(@"retainCount:%d",retainCount);
    }
    self.hidden = NO;
    contentLabel.text = title;
    [busyView startAnimating];
}

- (BOOL)isAnimating {
    return [busyView isAnimating];
}

- (void)stop {
    if (isRetain) {    
        retainCount--;
        ASLOC(@"retainCount:%d",retainCount);
        if (retainCount <= 0) {
            retainCount = 0;
            [busyView stopAnimating];
            self.hidden = YES;
        }
    } else {
        [busyView stopAnimating];
        self.hidden = YES;
    }
}

- (void)dealloc {
	[busyView removeFromSuperview];
	[busyView release];
	busyView = nil;
	[contentLabel removeFromSuperview];
	[contentLabel release];
	busyView = nil;
    [super dealloc];
}


@end
