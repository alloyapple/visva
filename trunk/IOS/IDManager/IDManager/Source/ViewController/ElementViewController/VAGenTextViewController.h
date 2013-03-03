//
//  VAGenTextViewController.h
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol VAGenTextDelegate;
@interface VAGenTextViewController : UIViewController<UITableViewDataSource,
    UITableViewDelegate>
@property(nonatomic, retain)NSString *currentText;
@property(nonatomic, assign)id<VAGenTextDelegate> delegate;
@end

@protocol VAGenTextDelegate <NSObject>
-(void)textGeneratorBack:(VAGenTextViewController*)vc;

@end
