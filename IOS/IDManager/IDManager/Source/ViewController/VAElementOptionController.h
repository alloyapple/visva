//
//  VAElementOptionController.h
//  IDManager
//
//  Created by tranduc on 4/11/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VAElementId.h"
#import "VAElementViewController.h"
@interface VAElementOptionController : UIViewController
@property(nonatomic, retain)VAElementId *selectedElement;
@property(nonatomic, assign)id<VAElementViewDelegate, TDWebViewDelegate> elementDelegate;
@end
