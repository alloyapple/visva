//
//  VAChooseIconViewController.m
//  IDManager
//
//  Created by tranduc on 2/23/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAChooseIconViewController.h"
#import "UIGridViewCell.h"
#import "TDDatabase.h"
#import "TDImageEncrypt.h"
#import "TDCommonLibs.h"
#import "VAEditImageViewController.h"
#import "TDAlert.h"

@interface VAGridViewCell : UIGridViewCell
@property(nonatomic, retain)UIImageView *iconImage;
@property(nonatomic, retain)UIImageView *selectedImage;

@end

@implementation VAGridViewCell
-(id)init{
    if (self = [super init]) {
        self.iconImage = [[[UIImageView alloc] init] autorelease];
        [self addSubview:_iconImage];
        self.selectedImage = [[[UIImageView alloc] init] autorelease];
        [self addSubview:_selectedImage];
    }
    return self;
}
-(void)dealloc{
    [_iconImage release];
    [_selectedImage release];
    [super dealloc];
}

@end


@interface VAIconsCatalogy: NSObject
@property(nonatomic, retain)NSMutableArray *listIcons;
@property(nonatomic, retain)NSString *catName;

@end

@implementation VAIconsCatalogy
-(void)dealloc{
    [_listIcons release];
    [_catName release];
    [super dealloc];
}
+(VAIconsCatalogy*)catIconsfromDic:(NSDictionary*)dic
{
    VAIconsCatalogy *cat = [[[VAIconsCatalogy alloc] init] autorelease];
    cat.catName = [dic objectForKey:@"name"];
    cat.listIcons = [NSMutableArray arrayWithArray:[dic objectForKey:@"ics"]];
    return cat;
}
-(NSMutableDictionary*)toDic{
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:2];
    [dic setObject:_catName forKey:@"name"];
    [dic setObject:_listIcons forKey:@"ics"];
    return dic;
}

+(NSMutableArray *)listCatFromArray:(NSArray*)arr{
    NSMutableArray * listIcons = [NSMutableArray arrayWithCapacity:arr.count];
    for (NSDictionary *dic in arr) {
        VAIconsCatalogy *cat = [VAIconsCatalogy catIconsfromDic:dic];
        [listIcons addObject:cat];
    }
    return listIcons;
}

+(NSMutableArray*)arrayFromListCat:(NSArray*)listCat{
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:listCat.count];
    for (VAIconsCatalogy *cat in listCat) {
        [array addObject:[cat toDic]];
    }
    return array;
}
@end

typedef struct {
    bool isSelect;
    int section;
    int index;
}kSelectedIcon;
@interface VAChooseIconViewController (){
    kSelectedIcon _selectedIcon;
    BOOL _bIsEditListIcon;
}
@property(nonatomic, retain)NSMutableArray *listIcons;
@property(nonatomic, retain)VAGridViewCell *selectedCell;

- (IBAction)btBackPressed:(id)sender;
- (IBAction)btEditPressed:(id)sender;
- (IBAction)btTrashPressed:(id)sender;

@end

@implementation VAChooseIconViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        _selectedIcon.isSelect = NO;
        _bIsEditListIcon = NO;
    }
    return self;
}
#define kFileNameListIcon @"Thumb/Z_list/listIcon.dat"
- (void)viewDidLoad
{
    [super viewDidLoad];
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    NSArray *array = [TDImageEncrypt listIcon: kFileNameListIcon];
    self.listIcons = [VAIconsCatalogy listCatFromArray:array];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationDidEnterBackground:)
                                                 name:UIApplicationDidEnterBackgroundNotification object:nil];
}
-(void)applicationDidEnterBackground:(NSNotification*)notifi
{
    [self updateListIcon];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - gridview
- (int) numberOfSection:(UIGridView*)grid{
    return _listIcons.count;
}
#define kNumCollumn 5
#define kHeighRow 50
#define kWidthRow (320/kNumCollumn)
- (CGFloat) gridView:(UIGridView *)grid widthForColumnAt:(int)columnIndex{
    return kWidthRow;
}
- (CGFloat) gridView:(UIGridView *)grid heightForRowAt:(int)rowIndex{
    return kHeighRow;
}

- (NSInteger) numberOfColumnsOfGridView:(UIGridView *) grid section:(int)section{
    return kNumCollumn;
}
- (NSInteger) numberOfCellsOfGridView:(UIGridView *) grid section:(int)section{
    VAIconsCatalogy *cat = [_listIcons objectAtIndex:section];
    return cat.listIcons.count;
}

#define kHeaderSectionHeight 30
-(float) gridView:(UIGridView *)grid heightHeaderSection:(int)section{
    return kHeaderSectionHeight;
}
-(NSString*)gridView:(UIGridView *)grid titleHeaderSection:(int)section{
    VAIconsCatalogy *cat = [_listIcons objectAtIndex:section];
    
    return cat.catName;
}
-(UIView*)gridView:(UIGridView *)grid viewHeaderSection:(int)section{
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 320, kHeaderSectionHeight)];
    VAIconsCatalogy *cat = [_listIcons objectAtIndex:section];
    label.text =  TDLocStrOne(cat.catName);
    label.backgroundColor = [UIColor clearColor];
    return label;
}

- (UIGridViewCell *) gridView:(UIGridView *)grid cellForRowAt:(int)rowIndex AndColumnAt:(int)columnIndex section:(int)section{
    VAGridViewCell *cell = (VAGridViewCell*)[grid dequeueReusableCell];
    if (cell == nil) {
        cell = [[[VAGridViewCell alloc] init] autorelease];
    }
    
    VAIconsCatalogy *cat = [_listIcons objectAtIndex:section];
    int index = rowIndex*kNumCollumn+columnIndex;
    NSString *fileName = [cat.listIcons objectAtIndex:index];
    
    float offsetx = 5, offsety = 5;
    CGRect f = CGRectMake(offsetx, offsetx,kWidthRow - offsetx*2 , kHeighRow-offsety*2);
    cell.iconImage.frame = f;
    cell.selectedImage.frame = f;
    UIImage *im = [TDImageEncrypt imageWithName:fileName];
    if (!im) {
        TDLOG(@"Error im icon empty %@", fileName);
    }
    cell.iconImage.image = im;
    BOOL isSelect = (_selectedIcon.isSelect && _selectedIcon.section == section
                     && _selectedIcon.index == index);
    [self selectCell:cell isSelect:isSelect];
    return cell;
}
-(void)selectCell:(VAGridViewCell*)cell isSelect:(BOOL)isS{
    
    /*if (isS)
    {
        cell.selectedImage.image = [UIImage imageNamed:@"check-mark.png"];
    }else*/
    {
        cell.selectedImage.image = nil;
    }
    
}
-(void)chooseIcon:(NSString*)path{
    self.currentIconPath = path;
    [_chooseIcDelegate chooseIconSave:self];
}
-(void)gridView:(UIGridView *)grid didSelect:(UIGridViewCell *)cell{
    int index = cell.rowIndex * kNumCollumn + cell.colIndex;
    
    VAIconsCatalogy *cat = [_listIcons objectAtIndex:cell.section];
    NSString *file= [cat.listIcons objectAtIndex:index];
    [self chooseIcon:file];
    /*
     
    _selectedIcon.isSelect = YES;
    _selectedIcon.section = cell.section;
    _selectedIcon.index = index;
    
    if (_selectedCell) {
        [self selectCell:_selectedCell isSelect:NO];
    }
    self.selectedCell = (VAGridViewCell*)cell;
    [self selectCell:_selectedCell isSelect:YES];
     */
}
- (NSString*)fileNameSelected{
    if (_selectedIcon.isSelect) {
        VAIconsCatalogy *cat = [_listIcons objectAtIndex:_selectedIcon.section];
        NSString *file= [cat.listIcons objectAtIndex:_selectedIcon.index];
        return file;
    }
    return nil;
}
- (IBAction)btBackPressed:(id)sender {
    /*
    self.currentIconPath = [self fileNameSelected];
    [self updateListIcon];
    if (self.currentIconPath) {
        [_chooseIcDelegate chooseIconSave:self];
        return;
    }
    */
    [_chooseIcDelegate chooseIconCancel:self];
}

- (IBAction)btEditPressed:(id)sender {
    //NSString *str = [self fileNameSelected];
    NSString *str = self.currentIconPath;
    if (str) {
        VAEditImageViewController *vc = [[[VAEditImageViewController alloc] initWithNibName:
                                         @"VAEditImageViewController" bundle:nil] autorelease];
        vc.sCurrentImagePath = str;
        vc.delegate = self;
        [self presentModalViewController:vc animated:YES];
    }else{
        [TDAlert showMessageWithTitle:TDLocStrOne(@"NoImageChoosed") message:nil delegate:self];
    }
    
}

- (IBAction)btTrashPressed:(id)sender
{
    [self chooseIcon:nil];
    /*
    if (_selectedIcon.isSelect) {
        VAIconsCatalogy *cat = [_listIcons objectAtIndex:_selectedIcon.section];
        NSString *str = [cat.listIcons objectAtIndex:_selectedIcon.index];
        [TDImageEncrypt deleteImage:str];
        [cat.listIcons removeObjectAtIndex:_selectedIcon.index];
        _selectedIcon.isSelect = false;
        _bIsEditListIcon = YES;
        [_gvListIcon reloadData];
    }else{
            //warning here
        
    }
     */
}
#pragma mark - EditImage delegate
-(void)updateListIcon{
    return;
    if (_bIsEditListIcon) {
        _bIsEditListIcon = NO;
        [TDImageEncrypt saveListIcon:[VAIconsCatalogy arrayFromListCat:_listIcons]
                                  to:kFileNameListIcon];
    }
}
-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    
}
-(void)editImageCancel:(VAEditImageViewController *)vc{
    [vc dismissModalViewControllerAnimated:YES];
    
}
-(void)editImageAccept:(VAEditImageViewController *)vc{
    self.currentIconPath = vc.sCurrentImagePath;
    if (vc.isAddNewImage) {
        //VAIconsCatalogy *cat = [_listIcons lastObject];
        //[cat.listIcons addObject:_currentIconPath];
        _bIsEditListIcon = YES;
    }
    
    [vc dismissModalViewControllerAnimated:YES];
    
    [self updateListIcon];
    [_chooseIcDelegate chooseIconSave:self];
    
}

- (void)dealloc {
    [_gvListIcon release];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setGvListIcon:nil];
    [super viewDidUnload];
}
@end
