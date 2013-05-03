//
//  TDGDrive.m
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDGDrive.h"
#import "TDCommonLibs.h"
@interface TDGDrive()
@property(nonatomic, retain)GTLServiceDrive *serviceDrive;

@end

static NSString *const kKeychainItemName = @"GoogleDriverIDManagerTD";
static NSString *const kClientID = @"559975448423.apps.googleusercontent.com";
static NSString *const kClientSecret = @"_akH-rF6T8TVuWtceKdRBUw5";
NSString *kGDriverChangeAuthenStatus = @"GDriveChangeAuthen";
@implementation TDGDrive
static TDGDrive *instance;

+(GTLServiceDrive*)shareService{
    return [TDGDrive shareInstance].serviceDrive;
}
+(TDGDrive*)shareInstance{
    if (instance == nil) {
        instance = [[TDGDrive alloc] init];
    }
    return instance;
}
-(id)init{
    if (self = [super init]) {
        self.serviceDrive = [[[GTLServiceDrive alloc] init] autorelease];
        self.serviceDrive.authorizer = [GTMOAuth2ViewControllerTouch authForGoogleFromKeychainForName:kKeychainItemName clientID:kClientID clientSecret:kClientSecret];
    }
    return self;
}

- (BOOL)isAuthorized
{
    return [((GTMOAuth2Authentication *)self.serviceDrive.authorizer) canAuthorize];
}

// Creates the auth controller for authorizing access to Googel Drive.
- (GTMOAuth2ViewControllerTouch *)createAuthController
{
    GTMOAuth2ViewControllerTouch *authController;
    authController = [[GTMOAuth2ViewControllerTouch alloc] initWithScope:kGTLAuthScopeDriveFile
                                                                clientID:kClientID
                                                            clientSecret:kClientSecret
                                                        keychainItemName:kKeychainItemName
                                                                delegate:self
                                                        finishedSelector:@selector(viewController:finishedWithAuth:error:)];
    return authController;
}

// Handle completion of the authorization process, and updates the Drive service
// with the new credentials.
- (void)viewController:(GTMOAuth2ViewControllerTouch *)viewController
      finishedWithAuth:(GTMOAuth2Authentication *)authResult
                 error:(NSError *)error
{
    if (error != nil)
    {
        if ([error code] != kGTMOAuth2ErrorWindowClosed) {
            [self showAlert:@"Authentication Error" message:error.localizedDescription];
        }
        
        self.serviceDrive.authorizer = nil;
    }
    else
    {
        self.serviceDrive.authorizer = authResult;
    }
    NSNotification *notifi = [NSNotification notificationWithName:kGDriverChangeAuthenStatus object:nil];
    [[NSNotificationCenter defaultCenter] postNotification:notifi];
    [viewController dismissModalViewControllerAnimated:YES];
}
-(void)showAlert:(NSString*)title message:(NSString*)mess{
    UIAlertView *al = [[UIAlertView alloc] initWithTitle:title message:mess delegate:self cancelButtonTitle:TDLocStrOne(@"OK") otherButtonTitles: nil];
    [al show];
}
-(void)authorizedFrom:(UIViewController *)vc{
    UIViewController *gvc = [self createAuthController];
    if (vc.navigationController) {
        [vc.navigationController pushViewController:gvc animated:YES];
        vc.navigationController.navigationBarHidden = NO;
    }else{
        [vc presentModalViewController:gvc animated:YES];
    }
    
    
}
-(void)unlinkAll{
    [GTMOAuth2ViewControllerTouch removeAuthFromKeychainForName:kKeychainItemName];
    self.serviceDrive.authorizer = nil;
}
@end
