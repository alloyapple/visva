//
//  VAEditImageViewController.m
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAEditImageViewController.h"
#import "TDImageEncrypt.h"
#import "TDCommonLibs.h"
#import "TDDatabase.h"
#import "TDImageEncrypt.h"
#import "MPAnimation.h"

@interface TDRectView : UIView
@property(nonatomic, assign)CGRect drawRect;

@end

@implementation TDRectView

-(void)drawRect:(CGRect)rect{
    [super drawRect:rect];
    CGContextRef context = UIGraphicsGetCurrentContext();
    // Add a color for red up where the colors are
    CGColorRef redColor = [UIColor colorWithRed:1.0 green:0.0
                                           blue:0.0 alpha:1.0].CGColor;
    
    // Add down at the bottom
    CGRect strokeRect = _drawRect;
    
    CGContextSetStrokeColorWithColor(context, redColor);
    CGContextSetLineWidth(context, 1.0);
    CGContextStrokeRect(context, strokeRect);
    
}

@end

@interface VAEditImageViewController (){
    BOOL _isAddNewImage;
}
@property (retain, nonatomic) IBOutlet TDRectView *rectView;
@property (retain, nonatomic) IBOutlet UIImageView *imView;
@property (retain, nonatomic) IBOutlet UIScrollView *svImage;
@property (nonatomic, retain) UIImage *currentImage;
@property (retain, nonatomic) IBOutlet UIButton *btBetween;
@property (retain, nonatomic) IBOutlet UIButton *btCheckTop;

@property (retain, nonatomic) IBOutlet UIButton *btBottomCheck;



- (IBAction)btLibrarySeleted:(id)sender;
- (IBAction)btCheckSeleted:(id)sender;
- (IBAction)btBackPressed:(id)sender;
- (IBAction)btWebPressed:(id)sender;

@end

@implementation VAEditImageViewController
@synthesize isAddNewImage = _isAddNewImage;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        _isAddNewImage = NO;
        _type = kTypeChooseIcon;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    
    [self updateDrawRect];
    UIImage *image = [TDImageEncrypt imageWithName:_sCurrentImagePath];
    _imView.image = image;
    
    
    UITapGestureRecognizer *doubleTapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(scrollViewDoubleTapped:)];
    doubleTapRecognizer.numberOfTapsRequired = 2;
    doubleTapRecognizer.numberOfTouchesRequired = 1;
    [self.svImage addGestureRecognizer:doubleTapRecognizer];
    
    UITapGestureRecognizer *twoFingerTapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(scrollViewTwoFingerTapped:)];
    twoFingerTapRecognizer.numberOfTapsRequired = 1;
    twoFingerTapRecognizer.numberOfTouchesRequired = 2;
    [self.svImage addGestureRecognizer:twoFingerTapRecognizer];
    [self setUpZoomScale];
    
    if (self.type == kTypeChooseIcon) {
        _btCheckTop.hidden = YES;
        
    }else{
        _rectView.hidden = YES;
        [_btBetween setImage:[UIImage imageNamed:@"camera-icon.png"] forState:UIControlStateNormal];
        [_btBetween setImage:[UIImage imageNamed:@"camera-icon_push.png"] forState:UIControlStateHighlighted];
        
        [_btBottomCheck setImage:[UIImage imageNamed:@"trash-icon.png"] forState:UIControlStateNormal];
        [_btBottomCheck setImage:[UIImage imageNamed:@"trash-icon_push.png"] forState:UIControlStateHighlighted];
    }
}
-(void)updateDrawRect{

    float width = 320, height = 90*320/120;
    float x = (320 -width)*0.5f;
    float y = (_rectView.frame.size.height - height)*0.5f;
    _rectView.drawRect = CGRectMake(x, y, width, height);
    [_rectView drawRect];
}
-(void)setUpZoomScale{
    _svImage.contentSize = _imView.image.size;
    _svImage.contentInset = (UIEdgeInsets){0,0,0,0};
    
    self.imView.frame = (CGRect){.origin=CGPointMake(0.0f, 0.0f), .size=_imView.image.size};
    
    // Set up the minimum & maximum zoom scales
    CGRect scrollViewFrame = self.svImage.frame;
    CGFloat scaleWidth = scrollViewFrame.size.width / self.svImage.contentSize.width;
    CGFloat scaleHeight = scrollViewFrame.size.height / self.svImage.contentSize.height;
    CGFloat minScale = MIN(scaleWidth, scaleHeight);
    
    self.svImage.minimumZoomScale = minScale;
    self.svImage.maximumZoomScale = 10.0f;
    self.svImage.zoomScale = minScale;
    
    [self centerScrollViewContents];
}
-(void)viewDidLayoutSubviews{
    [self updateDrawRect];
    [super viewDidLayoutSubviews];
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    //[self setUpZoomScale];
}

- (void)scrollViewDoubleTapped:(UITapGestureRecognizer*)recognizer {
    // Get the location within the image view where we tapped
    CGPoint pointInView = [recognizer locationInView:self.imView];
    
    // Get a zoom scale that's zoomed in slightly, capped at the maximum zoom scale specified by the scroll view
    CGFloat newZoomScale = self.svImage.zoomScale * 1.5f;
    newZoomScale = MIN(newZoomScale, self.svImage.maximumZoomScale);
    
    // Figure out the rect we want to zoom to, then zoom to it
    CGSize scrollViewSize = self.svImage.bounds.size;
    
    CGFloat w = scrollViewSize.width / newZoomScale;
    CGFloat h = scrollViewSize.height / newZoomScale;
    CGFloat x = pointInView.x - (w / 2.0f);
    CGFloat y = pointInView.y - (h / 2.0f);
    
    CGRect rectToZoomTo = CGRectMake(x, y, w, h);
    
    [self.svImage zoomToRect:rectToZoomTo animated:YES];
}

- (void)scrollViewTwoFingerTapped:(UITapGestureRecognizer*)recognizer {
    // Zoom out slightly, capping at the minimum zoom scale specified by the scroll view
    CGFloat newZoomScale = self.svImage.zoomScale / 1.5f;
    newZoomScale = MAX(newZoomScale, self.svImage.minimumZoomScale);
    [self.svImage  setZoomScale:newZoomScale animated:YES];
}
- (void)centerScrollViewContents {
    CGSize boundsSize = self.svImage.bounds.size;
    CGRect contentsFrame = self.imView.frame;
    
    if (contentsFrame.size.width < boundsSize.width) {
        contentsFrame.origin.x = (boundsSize.width - contentsFrame.size.width) / 2.0f;
    } else {
        contentsFrame.origin.x = 0.0f;
    }
    
    if (contentsFrame.size.height < boundsSize.height) {
        contentsFrame.origin.y = (boundsSize.height - contentsFrame.size.height) / 2.0f;
    } else {
        contentsFrame.origin.y = 0.0f;
    }
    
    self.imView.frame = contentsFrame;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc {
    [_rectView release];
    [_imView release];
    [_svImage release];
    [_btBetween release];
    [_btBottomCheck release];
    [_btCheckTop release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setRectView:nil];
    [self setImView:nil];
    [self setSvImage:nil];
    [self setBtBetween:nil];
    [self setBtBottomCheck:nil];
    [self setBtCheckTop:nil];
    [super viewDidUnload];
}
-(void)showImagePicker:(UIImagePickerControllerSourceType)type
{
    UIImagePickerController *vc = [[[UIImagePickerController alloc] init] autorelease];
    vc.sourceType = type;
    vc.allowsEditing = YES;
    vc.delegate = self;
    [self presentModalViewController:vc animated:YES];
}

#pragma mark - scrollView delegate
- (UIView*)viewForZoomingInScrollView:(UIScrollView *)scrollView {
    // Return the view that we want to zoom
    return self.imView;
}

- (void)scrollViewDidZoom:(UIScrollView *)scrollView {
    // The scroll view has zoomed, so we need to re-center the contents
    [self centerScrollViewContents];
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
        self.currentImage = image;
        self.imView.image = self.currentImage;
        _isAddNewImage = YES;
        [self setUpZoomScale];
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

- (IBAction)btLibrarySeleted:(id)sender
{
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary])
    {
        [self showImagePicker:UIImagePickerControllerSourceTypePhotoLibrary];
    }
}
- (UIImage *)cropImage{
    CGRect rect = [_imView convertRect:_rectView.drawRect fromView:_rectView];
    UIImage *image = [MPAnimation cropImage:_imView.image rect:rect];
    return image;
}
- (UIImage *)normalImage{
    return _imView.image;
}
- (IBAction)btCheckSeleted:(id)sender {
    if (_type == kTypeChooseIcon) {
        //choose image
        [self saveImage];
    }else{
        //delete image
        self.sCurrentImagePath = nil;
    }
    [_delegate editImageAccept:self];
}
-(void)saveImage{
    NSString *fileName = @"Thumb/OtherImage";
    [TDDatabase createDirecteryInDocument:fileName];
    
    NSDate *date = [NSDate date];
    fileName = [fileName stringByAppendingFormat:@"/image%d_%f.png",
                arc4random()%100000, [date timeIntervalSince1970]];
    
    UIImage *cropImage;
    if (_type == kTypeChooseIcon) {
        cropImage = [self cropImage];
    }else{
        cropImage = [self normalImage];
    }
    [TDImageEncrypt saveImage:cropImage fileName:fileName];
    [TDImageEncrypt saveImageNoPass:cropImage fileName:[fileName stringByAppendingString:@".png"]];
    
    self.sCurrentImagePath = fileName;
    _isAddNewImage = YES;
}
- (IBAction)btTopCheckSelected:(id)sender {
    if (_type == kTypeChooseMemoImage) { //only visible in choose image mode
        [self saveImage];
        [_delegate editImageAccept:self];
    }else{
        TDLOG(@"ERROR - top check is visible");
    }
}

- (IBAction)btBackPressed:(id)sender {
    [_delegate editImageCancel:self];
}
#define kTagWebImage 171
- (IBAction)btWebPressed:(id)sender
{
    if (self.type == kTypeChooseMemoImage) {
        [self btCamSeleted:nil];
        return;
    }
    TDWebViewController *web = [[[TDWebViewController alloc] initWithNibName:@"TDWebViewController" bundle:nil] autorelease];
    web.iTag = kTagWebImage;
    web.sUrlStart = @"http://google.com";
    web.webDelegate = self;
    web.bIsTakeScreenShot = YES;
    [self presentModalViewController:web animated:YES];
}
-(void)browserBack:(TDWebViewController *)controller{
    [self setNewCurrentImage:controller.screenShot];
    [controller dismissModalViewControllerAnimated:YES];
}
@end
