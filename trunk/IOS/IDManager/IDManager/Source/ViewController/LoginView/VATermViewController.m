//
//  VATermViewController.m
//  IDManager
//
//  Created by tranduc on 1/30/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VATermViewController.h"
#import "VAGlobal.h"
#import "VAConstan.h"
@interface VATermViewController ()
- (IBAction)btAccept:(id)sender;

@end

@implementation VATermViewController

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
    NSString *term = S_TERM_EN;
    NSString *lang = [[NSLocale preferredLanguages] objectAtIndex:0];
    if ([lang isEqualToString:@"ja"]) {
        term = S_TERM_JA;
    }
    _lbTerm.text = term;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btAccept:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}
- (IBAction)btCancel:(id)sender {
    exit(0);
}

- (void)dealloc {
    [_lbTerm release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setLbTerm:nil];
    [super viewDidUnload];
}
@end
