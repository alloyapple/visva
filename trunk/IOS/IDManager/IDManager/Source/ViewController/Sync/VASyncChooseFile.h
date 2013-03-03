//
//  VASyncChooseFile.h
//  IDManager
//
//  Created by tranduc on 3/1/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VADropboxViewController.h"
#import "TDSyn.h"
@interface VASyncChooseFile : UIViewController<UITableViewDataSource, UITableViewDelegate,TDSyncDelegate, UIAlertViewDelegate>
- (IBAction)btBackPressed:(id)sender;
@property (assign, nonatomic) kTypeCloud typeCloud;


@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) IBOutlet UITableView *tbListFile;

@end
