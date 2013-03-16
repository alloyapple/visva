//
//  VARootViewController.m
//  IDManager
//
//  Created by tranduc on 2/28/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VARootViewController.h"
#import "TDCommonLibs.h"
#import "VAGlobal.h"

@interface VARootViewController ()
@property(nonatomic, retain)UINavigationController *naviController;
@end

#pragma mark - iAd::begin
#import <iAd/iAd.h>
@interface VARootViewController()<ADBannerViewDelegate>
@property(nonatomic, retain)ADBannerView *bannerView;
@property(nonatomic, retain)UIView *contentView;
@end
#pragma mark iAd::end - 

@implementation VARootViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.naviController = [[[UINavigationController alloc] initWithRootViewController:self.viewController] autorelease];
    [self.view addSubview:self.naviController.view];
    [self addChildViewController:self.naviController];
    [self initiAdBaner];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)dealloc{
    //iAd
    [_bannerView release];
    [_contentView release];
    [super dealloc];
}

#pragma mark - iAd::begin
-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    [self layoutAnimated:NO];
}
-(void)viewDidLayoutSubviews{
    [super viewDidLayoutSubviews];
    [self layoutAnimated:NO];
}

-(void)initiAdBaner{
    if ([VAGlobal share].appSetting.isUnlockHideIad) {
        return;
    }
    if ([ADBannerView instancesRespondToSelector:@selector(initWithAdType:)]) {
        _bannerView = [[ADBannerView alloc] initWithAdType:ADAdTypeBanner];
    } else {
        _bannerView = [[ADBannerView alloc] init];
    }
    _bannerView.delegate = self;
    _bannerView.frame = CGRectMake(0, 410, 320, 50);
    [self.view addSubview:_bannerView];
    self.contentView = self.naviController.view;
}
- (void)layoutAnimated:(BOOL)animated
{
    // As of iOS 6.0, the banner will automatically resize itself based on its width.
    // To support iOS 5.0 however, we continue to set the currentContentSizeIdentifier appropriately.
    TDLOG(@"LayoutBanner = %d", animated);
    CGRect contentFrame = self.view.bounds;
    if (contentFrame.size.width < contentFrame.size.height) {
        _bannerView.currentContentSizeIdentifier = ADBannerContentSizeIdentifierPortrait;
    } else {
        _bannerView.currentContentSizeIdentifier = ADBannerContentSizeIdentifierLandscape;
    }
    
    CGRect bannerFrame = _bannerView.frame;
    if (_bannerView.bannerLoaded && ![VAGlobal share].appSetting.isUnlockHideIad) {
    contentFrame.size.height = contentFrame.size.height - bannerFrame.size.height;
    bannerFrame.origin.y = contentFrame.size.height;
    } else {
         bannerFrame.origin.y = contentFrame.size.height;
    }
    if (animated) {
        [UIView animateWithDuration: 0.25 animations:^{
            _contentView.frame = contentFrame;
            [_contentView layoutIfNeeded];
            _bannerView.frame = bannerFrame;
        }];
    }else{
        _contentView.frame = contentFrame;
        [_contentView layoutIfNeeded];
        _bannerView.frame = bannerFrame;
    }
    
}
#pragma mark iAd Delegate
// This method is invoked when the banner has confirmation that an ad will be presented, but before the ad
// has loaded resources necessary for presentation.
- (void)bannerViewWillLoadAd:(ADBannerView *)banner {
    TDLOG(@"Banner will loadad");
    
}

// This method is invoked each time a banner loads a new advertisement. Once a banner has loaded an ad,
// it will display that ad until another ad is available. The delegate might implement this method if
// it wished to defer placing the banner in a view hierarchy until the banner has content to display.
- (void)bannerViewDidLoadAd:(ADBannerView *)banner{
    TDLOG(@" banner loadad");
    [self layoutAnimated:YES];
}

// This method will be invoked when an error has occurred attempting to get advertisement content.
// The ADError enum lists the possible error codes.
- (void)bannerView:(ADBannerView *)banner didFailToReceiveAdWithError:(NSError *)error{
    TDLOGERROR(@"ErrorBanerViewiAd=%@", error);
    [self layoutAnimated:YES];
}

// This message will be sent when the user taps on the banner and some action is to be taken.
// Actions either display full screen content in a modal session or take the user to a different
// application. The delegate may return NO to block the action from taking place, but this
// should be avoided if possible because most advertisements pay significantly more when
// the action takes place and, over the longer term, repeatedly blocking actions will
// decrease the ad inventory available to the application. Applications may wish to pause video,
// audio, or other animated content while the advertisement's action executes.
- (BOOL)bannerViewActionShouldBegin:(ADBannerView *)banner willLeaveApplication:(BOOL)willLeave{
    return YES;
}

// This message is sent when a modal action has completed and control is returned to the application.
// Games, media playback, and other activities that were paused in response to the beginning
// of the action should resume at this point.
- (void)bannerViewActionDidFinish:(ADBannerView *)banner{
}
#pragma mark iAd::end -


@end
