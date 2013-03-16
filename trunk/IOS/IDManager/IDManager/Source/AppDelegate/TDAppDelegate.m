//
//  TDAppDelegate.m
//  IDManager
//
//  Created by tranduc on 1/15/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "TDAppDelegate.h"

#import "TDViewController.h"
#import "VALoginController.h"
#import "VAGlobal.h"
#import "TDCommonLibs.h"
#import "VARootViewController.h"

#pragma mark - Dropbox::begin
#import <DropboxSDK/DropboxSDK.h>
@interface TDAppDelegate () <DBSessionDelegate, DBNetworkRequestDelegate>
@property(nonatomic, retain)NSString *relinkDropboxUserId;
@end
#pragma mark Dropbox::end



static TDAppDelegate *instance = nil;
@implementation TDAppDelegate

- (void)dealloc
{
    [_window release];
    [_viewController release];
    instance = nil;
    [super dealloc];
}
+(TDAppDelegate*)share{
    return instance;
}

#pragma mark - Dropbox - begin
#define kDropboxAppKey @"yg3odz2tu18z6du"
#define kDropboxAppSecret @"pthw0zq8qkdf8ki"
#define kDropboxAppRoot kDBRootAppFolder // kDBRootAppFolder or kDBRootDropbox
NSString *kDropboxChangeLinkedStatus = @"TDDBChangeSt";

/*
-(void)initDropboxSync{
    DBAccountManager* accountMgr =
    [[DBAccountManager alloc] initWithAppKey:kDropboxAppKey secret:kDropboxAppSecret];
    [DBAccountManager setSharedManager:accountMgr];
    DBAccount *account = accountMgr.linkedAccount;
    
    if (account) {
        DBFilesystem *filesystem = [[DBFilesystem alloc] initWithAccount:account];
        [DBFilesystem setSharedFilesystem:filesystem];
    }
}

-(BOOL)dropboxHandleOpenUrlSync:(NSURL*)url{
    DBAccount *account = [[DBAccountManager sharedManager] handleOpenURL:url];
    if (account) {
        DBFilesystem *filesystem = [[DBFilesystem alloc] initWithAccount:account];
        [DBFilesystem setSharedFilesystem:filesystem];
        TDLOG(@"App linked successfully!");
        //NSNotification *notifi = [NSNotification notificationWithName:kDropboxChangeLinkedStatus object:nil];
        //[[NSNotificationCenter defaultCenter] postNotification:notifi];
        return YES;
    }
    return NO;
}
*/
-(void)initDropboxCore{
    DBSession* session =
    [[DBSession alloc] initWithAppKey:kDropboxAppKey appSecret:kDropboxAppSecret root:kDropboxAppRoot];
	session.delegate = self; // DBSessionDelegate methods allow you to handle re-authenticating
	[DBSession setSharedSession:session];
    [session release];
	
	[DBRequest setNetworkRequestDelegate:self];
}
-(BOOL)dropboxCoreHandleOpenURL:(NSURL*)url{
    return [[DBSession sharedSession] handleOpenURL:url];
}

#pragma mark DBSessionDelegate methods

- (void)sessionDidReceiveAuthorizationFailure:(DBSession*)session userId:(NSString *)userId {
	self.relinkDropboxUserId = userId;
	[[[[UIAlertView alloc]
	   initWithTitle:@"Dropbox Session Ended" message:@"Do you want to relink?" delegate:self
	   cancelButtonTitle:@"Cancel" otherButtonTitles:@"Relink", nil]
	  autorelease]
	 show];
}
#pragma mark UIAlertViewDelegate methods

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)index {
	if (index != alertView.cancelButtonIndex) {
		[[DBSession sharedSession] linkUserId:self.relinkDropboxUserId fromController:self.viewController];
	}
	self.relinkDropboxUserId = nil;
}
#pragma mark DBNetworkRequestDelegate methods

static int outstandingRequests;

- (void)networkRequestStarted {
	outstandingRequests++;
	if (outstandingRequests == 1) {
		[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
	}
}

- (void)networkRequestStopped {
	outstandingRequests--;
	if (outstandingRequests == 0) {
		[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
	}
}

#pragma mark Dropbox::end

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    instance = self;
    self.window = [[[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]] autorelease];
    // Override point for customization after application launch.
    self.viewController = [[[TDViewController alloc] initWithNibName:@"TDViewController" bundle:nil] autorelease];
    VARootViewController *root = [[[VARootViewController alloc] initWithNibName:@"VARootViewController" bundle:nil] autorelease];
    root.viewController = self.viewController;
    self.window.rootViewController = root;
    [self.window makeKeyAndVisible];
    [self initDropboxCore];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    [[VAGlobal share].appSetting saveSetting];
    [(TDApplicationIdle*)[UIApplication sharedApplication] appDidEnterBackground];
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    if ([VAGlobal share].appSetting.isSecurityOn) {
        [_viewController showReloginWindow];
    }else{
        [(TDApplicationIdle*)[UIApplication sharedApplication] appDidEnterForceGround];
    }
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}
-(BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation{
    return [self dropboxCoreHandleOpenURL:url];
}

@end
