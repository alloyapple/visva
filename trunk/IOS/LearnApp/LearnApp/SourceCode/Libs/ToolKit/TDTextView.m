//
//  TDTextViewPlaceHolder.m
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDTextView.h"


@interface TDTextView ()
- (void)additionInit;
- (void)updateShouldDrawPlaceholder;
- (void)textChanged:(NSNotification *)notification;
@end


@implementation TDTextView
#pragma mark - Accessors
@synthesize placeholder = _placeholder;
@synthesize placeholderTextColor = _placeholderTextColor;

- (void)setText:(NSString *)string {
	[super setText:string];
	[self updateShouldDrawPlaceholder];
}


- (void)setPlaceholder:(NSString *)string {
	if ([string isEqual:_placeholder]) {
		return;
	}
	[_placeholder release];
	_placeholder = [string retain];
	[self updateShouldDrawPlaceholder];
}


#pragma mark - NSObject

- (void)dealloc {
    [_placeholder release];
    [_placeholderTextColor release];
	[[NSNotificationCenter defaultCenter] removeObserver:self name:UITextViewTextDidChangeNotification object:self];
    [super dealloc];
}


#pragma mark - UIView

- (id)initWithCoder:(NSCoder *)aDecoder {
	if ((self = [super initWithCoder:aDecoder])) {
		[self additionInit];
	}
	return self;
}


- (id)initWithFrame:(CGRect)frame {
	if ((self = [super initWithFrame:frame])) {
		[self additionInit];
	}
	return self;
}


- (void)drawRect:(CGRect)rect {
	[super drawRect:rect];
	if (_shouldDrawPlaceholder) {
		[_placeholderTextColor set];
		[_placeholder drawInRect:CGRectMake(8.0f, 8.0f, self.frame.size.width - 16.0f, self.frame.size.height - 16.0f) withFont:self.font lineBreakMode:NSLineBreakByWordWrapping  alignment:self.textAlignment];
	}
}


#pragma mark - Private

- (void)additionInit {
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(textChanged:) name:UITextViewTextDidChangeNotification object:self];
	
	self.placeholderTextColor = [UIColor grayColor];
	_shouldDrawPlaceholder = NO;
}


- (void)updateShouldDrawPlaceholder {
	BOOL prev = _shouldDrawPlaceholder;
	_shouldDrawPlaceholder = self.placeholder && self.placeholderTextColor && self.text.length == 0;
	
	if (prev != _shouldDrawPlaceholder) {
		[self setNeedsDisplay];
	}
}


- (void)textChanged:(NSNotification *)notification {
	[self updateShouldDrawPlaceholder];
}
@end
