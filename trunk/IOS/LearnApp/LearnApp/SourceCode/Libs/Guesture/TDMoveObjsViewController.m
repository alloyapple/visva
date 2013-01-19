//
//  TDMoveObjsViewController.m
//  LearnApp
//
//  Created by tranduc on 1/19/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDMoveObjsViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "TDSize.h"

@interface TDMoveObjsViewController ()
@property(nonatomic, retain)NSMutableArray *listPiece;
@property(nonatomic, assign)UITapGestureRecognizer *tapGesture;
@end

@implementation TDMoveObjsViewController

-(id)initWithFrame:(CGRect)frame ListPiece:(NSArray *)arr{
    self = [super init];
    if (self) {
        self.listPiece = [NSMutableArray arrayWithArray:arr];
        TDDrawView *v = [[[TDDrawView alloc] initWithFrame:frame] autorelease];
        self.view = v;
        
        self.tapGesture= [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapView:)];
        [_tapGesture setDelegate:self];
        [self.view addGestureRecognizer:_tapGesture];
        
        [_tapGesture release];
        
        //list piece
        if (_listPiece == nil) {
            self.listPiece = [NSMutableArray array];
        }
        for (UIView *piece in _listPiece) {
            [self.view addSubview:piece];
            [self addGestureRecognizersToPiece:piece];
        }
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - gesture
#pragma mark Add gesture
// adds a set of gesture recognizers to one of our piece subviews
- (void)addGestureRecognizersToPiece:(UIView *)piece
{
    /* not rotate
    UIRotationGestureRecognizer *rotationGesture = [[UIRotationGestureRecognizer alloc] initWithTarget:self action:@selector(rotatePiece:)];
    [piece addGestureRecognizer:rotationGesture];
    */
    /*not pinch
    UIPinchGestureRecognizer *pinchGesture = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(scalePiece:)];
    [pinchGesture setDelegate:self];
    [piece addGestureRecognizer:pinchGesture];
    */
    [piece setUserInteractionEnabled:YES];
    UIPanGestureRecognizer *panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panPiece:)];
    [panGesture setMaximumNumberOfTouches:2];
    [panGesture setDelegate:self];
    [piece addGestureRecognizer:panGesture];
    //[_tapGesture requireGestureRecognizerToFail:panGesture];
    /*
    UILongPressGestureRecognizer *longPressGesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(showResetMenu:)];
    [piece addGestureRecognizer:longPressGesture];
     */
}

#pragma mark utilyti method
- (void)adjustAnchorPointForGestureRecognizer:(UIGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
        UIView *piece = gestureRecognizer.view;
        CGPoint locationInView = [gestureRecognizer locationInView:piece];
        CGPoint locationInSuperview = [gestureRecognizer locationInView:piece.superview];
        
        piece.layer.anchorPoint = CGPointMake(locationInView.x / piece.bounds.size.width, locationInView.y / piece.bounds.size.height);
        piece.center = locationInSuperview;
    }
}
#pragma mark gesture recognizer
-(void)tapView:(UITapGestureRecognizer*)gesture{
    if (_typeTouch == kTypeTouchMoveObj) {
        UIView *v = [_moveObjDelegate getAddView:self];
        if (v == nil) {
            return;
        }
        [self.view addSubview:v];
        v.center = [gesture locationInView:self.view];
        [_listPiece addObject:v];
        [self addGestureRecognizersToPiece:v];
    }
}
// shift the piece's center by the pan amount
// reset the gesture recognizer's translation to {0, 0} after applying so the next callback is a delta from the current position
- (void)panPiece:(UIPanGestureRecognizer *)gestureRecognizer
{
    UIView *piece = [gestureRecognizer view];
    //move obj mode
    if (_typeTouch == kTypeTouchMoveObj) {
        [self adjustAnchorPointForGestureRecognizer:gestureRecognizer];
        
        if ([gestureRecognizer state] == UIGestureRecognizerStateBegan || [gestureRecognizer state] == UIGestureRecognizerStateChanged) {
            CGPoint translation = [gestureRecognizer translationInView:[piece superview]];
            CGPoint locationInSupperView = CGPointMake([piece center].x + translation.x, [piece center].y + translation.y);
            
            if (CGRectContainsPoint(self.view.frame, locationInSupperView)) {
               [piece setCenter:locationInSupperView];
                [gestureRecognizer setTranslation:CGPointZero inView:[piece superview]];
            }
            
        }
    }else{ //draw mode
        CGPoint locationInSupperView = [gestureRecognizer locationInView:piece.superview];
        
        if (gestureRecognizer.state == UIGestureRecognizerStateBegan){
            [(TDDrawView*)self.view setTouchDrawPoint:  locationInSupperView];
            [(TDDrawView*)self.view  setIsDrawing: YES];
            [(TDDrawView*)self.view setTouchBeginPoint:locationInSupperView];
        }else if(gestureRecognizer.state == UIGestureRecognizerStateChanged) {
            if (CGRectContainsPoint(self.view.frame, locationInSupperView)) {
                [(TDDrawView*)self.view setTouchDrawPoint:  locationInSupperView];
            }
           
        }else if (gestureRecognizer.state == UIGestureRecognizerStateEnded || gestureRecognizer.state == UIGestureRecognizerStateCancelled){
            for (UIView *v in _listPiece) {
                if (CGRectContainsPoint(v.frame, locationInSupperView)) {
                    //connect point from a->b
                    [(TDDrawView*)self.view add:gestureRecognizer.view andView:v];
                    break;
                }
            }
            [(TDDrawView*)self.view  setIsDrawing: NO];
        }
    }
    [self.view setNeedsDisplay];
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
    if (_typeTouch == kTypeTouchDrawLine) {
        return NO;
    }
    
    // if the gesture recognizers are on different views, don't allow simultaneous recognition
    if (gestureRecognizer.view != otherGestureRecognizer.view)
        return NO;
    
    return YES;
}
@end

@interface TDDrawView()
@property(nonatomic, retain)NSMutableArray *listLine;
@property(nonatomic, retain)NSMutableArray *listSecond;
@end
@implementation TDDrawView
- (id)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _isDrawing = NO;
        self.backgroundColor = [UIColor blackColor];
        self.listLine = [NSMutableArray arrayWithCapacity:1];
        self.listSecond = [NSMutableArray arrayWithCapacity:2];
        self.clearsContextBeforeDrawing = YES;
    }
    return self;
}
-(void)dealloc{
    [_listLine release];
    [_listSecond release];
    [super dealloc];
}
-(void)add:(UIView *)v1 andView:(UIView *)v2{
    [_listLine addObject:v1];
    [_listSecond addObject:v2];
}
-(void)drawRect:(CGRect)rect{
    CGContextRef c = UIGraphicsGetCurrentContext();
    
    CGContextSetLineWidth(c, 3.0f);
    CGFloat red[4] = {1.0f, 0.0f, 0.0f, 1.0f};
    CGContextSetStrokeColor(c, red);
    if (_isDrawing) {
        CGContextMoveToPoint(c, _touchBeginPoint.x, _touchBeginPoint.y);
        CGContextAddLineToPoint(c, _touchDrawPoint.x,_touchDrawPoint.y);
        CGContextStrokePath(c);
    }
    for (int i = 0; i< _listLine.count; i++) {
        UIView *v1 = [_listLine objectAtIndex:i];
        UIView *v2 = [_listSecond objectAtIndex:i];
        CGPoint p1 = TDRealCenter(v1);
        CGPoint p2 = TDRealCenter(v2);
        CGContextMoveToPoint(c, p1.x, p1.y);
        CGContextAddLineToPoint(c, p2.x,p2.y);
        CGContextStrokePath(c);
    }
    
}

@end
