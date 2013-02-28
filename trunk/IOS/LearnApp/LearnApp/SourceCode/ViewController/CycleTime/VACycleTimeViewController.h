//
//  VACycleTimeViewController.h
//  LearnApp
//
//  Created by tranduc on 1/17/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VAProject.h"
#import "AVCamCaptureManager.h"
@interface VACycleTimeViewController : UIViewController<UIPickerViewDataSource, UIPickerViewDelegate, AVCamCaptureManagerDelegate>{
    BOOL _isSaving;
    BOOL _isNextCircleStep;
}

@property (nonatomic,retain) AVCamCaptureManager *captureManager;
@property (nonatomic,retain) AVCaptureVideoPreviewLayer *captureVideoPreviewLayer;

@end
