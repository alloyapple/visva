//
//  VAEditMemoImageViewController.m
//  IDManager
//
//  Created by tranduc on 3/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAEditMemoImageViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "TDImageEncrypt.h"
#import "TDCommonLibs.h"
#import "TDDatabase.h"
#import "MPAnimation.h"

#import <math.h>

@interface VAEditMemoImageViewController ()
@property (retain, nonatomic) IBOutlet UIView *vIconChoose;
@property (retain, nonatomic) IBOutlet UIToolbar *vTop;
@property (retain, nonatomic) IBOutlet UIToolbar *vBottom;

@property (retain, nonatomic) IBOutlet UIImageView *imView;


@property (nonatomic, assign)kTypePanBorder typePanBorder;
@property (nonatomic, assign)BOOL isTouchPan;

-(void)addGesture;
-(void)addGestureV2;
@end

@implementation VAEditMemoImageViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_vIconChoose release];
    [_imView release];
    [_vTop release];
    [_vBottom release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setVIconChoose:nil];
    [self setImView:nil];
    [self setVTop:nil];
    [self setVBottom:nil];
    [super viewDidUnload];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    rotate = 0;
    
    //image edit
    _vIconChoose.layer.borderWidth = 3;
    _vIconChoose.layer.borderColor = [[UIColor redColor] CGColor];
    
    [self addGestureV2];
    
    UIImage *image = [TDImageEncrypt imageWithName:_sCurrentImagePath];
    _imView.image = image;
    
}
-(void)addGesture
{
    UIPanGestureRecognizer *panGesture = [[[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panView:)] autorelease];
    panGesture.maximumNumberOfTouches = 1;
    panGesture.minimumNumberOfTouches = 1;
    [self.view addGestureRecognizer:panGesture];
    
    UIRotationGestureRecognizer *rotateGesture = [[[UIRotationGestureRecognizer alloc] initWithTarget:self action:@selector(onRotate:)] autorelease];
    rotateGesture.delegate = self;
    [self.view addGestureRecognizer:rotateGesture];
    
    
    UIPinchGestureRecognizer *pinchGesture = [[[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(onPinch:)] autorelease];
    [self.view addGestureRecognizer:pinchGesture];
    pinchGesture.delegate = self;
}

-(void)addGestureV2
{
    UIPanGestureRecognizer *panGesture = [[[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(onPanView:)] autorelease];
    
    panGesture.maximumNumberOfTouches = 1;
    panGesture.minimumNumberOfTouches = 1;
    [self.view addGestureRecognizer:panGesture];
    self.typePanBorder = kTypePanBorderNone;
    
}

- (void)onRotate:(UIRotationGestureRecognizer *)recognizer {
    //TDLOG(@"rotate = %f", recognizer.rotation);
    
    /*
    if (recognizer.rotation > 0) {
        _imView.transform = CGAffineTransformRotate(_imView.transform, recognizer.rotation);

    }
    recognizer.rotation = 0;
    return;
    */
    double r = recognizer.rotation;
    while( r < -M_PI )
        r += M_PI*2;
    
    while( r > M_PI )
        r -= M_PI*2;
    //r = modf(M_2_PI, &r);
    TDLOG(@"r=%f",r);
    rotate = r + rotate;
    if (rotate>=M_PI_2*0.6f) {
        _imView.transform = CGAffineTransformRotate(_imView.transform, M_PI_2);
        rotate = 0;
    }
     else if (rotate<= -1 * M_PI_2){
        //_imView.transform = CGAffineTransformRotate(_imView.transform, -1 * M_PI_2);
        rotate = 0;
    }
    [recognizer setRotation:0];
    return;
    if (recognizer.rotation>=M_PI_2) {
        _imView.transform = CGAffineTransformRotate(_imView.transform, M_PI_2);
        recognizer.rotation = 0;
    }else if (recognizer.rotation<= -1 * M_PI_2){
        _imView.transform = CGAffineTransformRotate(_imView.transform, -1 * M_PI_2);
        recognizer.rotation = 0;
    }
    
    return;
    _imView.transform = CGAffineTransformRotate(_imView.transform, recognizer.rotation);
    recognizer.rotation = 0;
    return;
    recognizer.view.transform = CGAffineTransformRotate(recognizer.view.transform, recognizer.rotation);
    
}
- (void)onPinch:(UIPinchGestureRecognizer *)recognizer {
    
    _vIconChoose.transform = CGAffineTransformScale(_vIconChoose.transform, recognizer.scale, recognizer.scale);
    recognizer.scale = 1;
    
}
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    return YES;
}
bool fuzzyEqual(float a, float b, float d)
{
    float d1 = a-b;
    if (-d <= d1 && d1 <=d) {
        return true;
    }
    return false;
}
-(void)onPanView:(UIPanGestureRecognizer*)gesture{
    CGPoint trans = [gesture translationInView:gesture.view];
    CGPoint pos = [gesture locationInView:gesture.view];
    pos = [_vIconChoose convertPoint:pos fromView:gesture.view];
    
    if (gesture.state == UIGestureRecognizerStateBegan) {
        
        self.isTouchPan = NO;
        
    }else if (gesture.state == UIGestureRecognizerStateChanged) {
        if (!self.isTouchPan) {
            if (fabs(trans.x) > fabs(trans.y)) { //left - right
                float delta = _vIconChoose.frame.size.width/2;
                if (pos.x < delta) {
                    _typePanBorder = kTypePanBorderLeft;
                }else{
                    _typePanBorder = kTypePanBorderRight;
                }
            }else{
                float delta = _vIconChoose.frame.size.height/2;
                if (pos.y < delta) {
                    _typePanBorder = kTypePanBorderTop;
                }else {
                    _typePanBorder = kTypePanBorderBottom;
                }
            }
            self.isTouchPan = YES;
            return;
        }
        if (!self.isTouchPan) {
            return;
        }
        
        //TDLOG(@"translate = %f, %f", pos.x, pos.y);
        CGRect oldFrame = _vIconChoose.frame;
        if (_typePanBorder == kTypePanBorderLeft) {
            oldFrame.origin.x += trans.x;
            oldFrame.size.width -= trans.x;
            if (oldFrame.size.width < 0) {
                _typePanBorder = kTypePanBorderRight;
            }
        }else if (_typePanBorder == kTypePanBorderRight){
            oldFrame.size.width += trans.x;
            if (oldFrame.size.width < 0) {
                _typePanBorder = kTypePanBorderLeft;
            }
        }else if (_typePanBorder == kTypePanBorderTop){
            oldFrame.origin.y += trans.y;
            oldFrame.size.height -= trans.y;
            if (oldFrame.size.height < 0) {
                _typePanBorder = kTypePanBorderBottom;
            }
        }else if (_typePanBorder == kTypePanBorderBottom){
            oldFrame.size.height += trans.y;
            if (oldFrame.size.height < 0) {
                _typePanBorder = kTypePanBorderTop;
            }
        }
        
        _vIconChoose.frame = oldFrame;
        [gesture setTranslation:CGPointMake(0, 0) inView:gesture.view];
    }
}

-(void)panView:(UIPanGestureRecognizer*)gesture{
    if (gesture.state == UIGestureRecognizerStateChanged) {
        CGPoint pos = [gesture translationInView:gesture.view];
        
        //TDLOG(@"translate = %f, %f", pos.x, pos.y);
        CGRect oldFrame = _vIconChoose.frame;
        CGPoint des = CGPointMake(pos.x + oldFrame.origin.x, pos.y + oldFrame.origin.y);
        if (des.x<0) {
            des.x = 0;
        }else if(des.x+oldFrame.size.width > _imView.frame.size.width){
            des.x = _imView.frame.size.width - oldFrame.size.width;
        }
        if (des.y<_imView.frame.origin.y) {
            des.y = _imView.frame.origin.y;
        }else if(des.y+oldFrame.size.height > _imView.frame.origin.y + _imView.frame.size.height){
            des.y = _imView.frame.origin.y + _imView.frame.size.height - oldFrame.size.height;
        }
        oldFrame.origin = des;
        _vIconChoose.frame = oldFrame;
        [gesture setTranslation:CGPointMake(0, 0) inView:gesture.view];
    }
}


-(void)showImagePicker:(UIImagePickerControllerSourceType)type
{
    UIImagePickerController *vc = [[[UIImagePickerController alloc] init] autorelease];
    vc.sourceType = type;
    vc.allowsEditing = YES;
    vc.delegate = self;
    [self presentModalViewController:vc animated:YES];
}

#pragma mark - imagePicker
-(void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    TDLOG(@"info = %@", info);
    UIImage *image = [info objectForKey:UIImagePickerControllerEditedImage];
    if (!image) {
        image = [info objectForKey:UIImagePickerControllerOriginalImage];
    }
    [self setNewCurrentImage:image];
    [picker dismissModalViewControllerAnimated:YES];
}
-(void)setNewCurrentImage:(UIImage*)image{
    if (image) {
        self.imView.image = image;
    }
}
-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissModalViewControllerAnimated:YES];
}
- (IBAction)btCamSeleted:(id)sender {
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        [self showImagePicker:UIImagePickerControllerSourceTypeCamera];
    }
}



- (IBAction)btBack:(id)sender {
    [_delegate editMemoDidCancel:self];
}

- (IBAction)btTopCheck:(id)sender {
    [self saveImage];
    [_delegate editMemoDidSave:self];
}
- (IBAction)btCamera:(id)sender {
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        [self showImagePicker:UIImagePickerControllerSourceTypeCamera];
    }
}
- (IBAction)btAlbum:(id)sender {
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary])
    {
        [self showImagePicker:UIImagePickerControllerSourceTypePhotoLibrary];
    }
}

- (IBAction)btDelete:(id)sender {
    self.sCurrentImagePath = nil;
    [_delegate editMemoDidSave:self];
}

-(void)saveImage{
    NSString *fileName = @"Thumb/OtherImage";
    [TDDatabase createDirecteryInDocument:fileName];
    
    NSDate *date = [NSDate date];
    fileName = [fileName stringByAppendingFormat:@"/image%d_%f.png",
                arc4random()%100000, [date timeIntervalSince1970]];
    
    UIImage *cropImage = [self cropImage];
    if (cropImage == nil) {
        TDLOGERROR(@"Image is nil");
        return;
    }
    [TDImageEncrypt saveImage:cropImage fileName:fileName];
    [TDImageEncrypt saveImageNoPass:cropImage fileName:[fileName stringByAppendingString:@".png"]];    
    self.sCurrentImagePath = fileName;
}


- (UIImage *)cropImage{
    //CGRect frame = _vIconChoose.frame;
    CGRect frame = [_vIconChoose bounds];
    frame.origin = CGPointZero;
    /*
    CGMutablePathRef path = CGPathCreateMutable();
    
    CGAffineTransform t = (_vIconChoose.transform);
    CGPathAddRect( path , &t, frame );
    frame = CGPathGetBoundingBox( path );
    CGPathRelease( path );
     */
    
    CGRect rect = [self.view convertRect:frame fromView:_vIconChoose];
    TDLOGRect(@"Rect is", rect);
    _vIconChoose.hidden = YES;
    _vTop.hidden = YES;
    _vBottom.hidden = YES;
    //UIImage *image = [MPAnimation cropImage:_imView.image rect:rect];
    UIImage *image = [MPAnimation renderImageFromView:self.view withRect:rect];
    _vIconChoose.hidden = NO;
    _vTop.hidden = NO;
    _vBottom.hidden = NO;
    return image;
}
- (UIImage *)normalImage{
    return _imView.image;
}
@end
