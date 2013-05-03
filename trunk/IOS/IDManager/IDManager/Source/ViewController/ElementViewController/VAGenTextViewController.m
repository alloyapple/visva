//
//  VAGenTextViewController.m
//  IDManager
//
//  Created by tranduc on 3/2/13.
//  Copyright (c) 2013 tranduc. All rights reserved.
//

#import "VAGenTextViewController.h"
#import "TDCommonLibs.h"
#import "TDAlert.h"

#pragma mark - model
typedef enum {
    kRandomTypeNumber=0,
    kRandomTypeCapital=1,
    kRandomTypeLowerCase=2,
    kRandomTypeSign=3
}kRandomType;
@interface TDTextRandom:NSObject
@property(nonatomic, retain)NSString *title;
@property(nonatomic, assign)BOOL isEnable;
@property(nonatomic, assign)kRandomType randType;
@property(nonatomic, retain)NSArray *randCharacter;
-(id)initWithType:(kRandomType)type;
@end

@implementation TDTextRandom
-(id)initWithType:(kRandomType)type{
    if (self =[super init]) {
        _randType = type;
        self.isEnable = true;
        [self initCharacters];
    }
    return self;
}

-(void)initCharacters
{
    if (self.randType == kRandomTypeNumber) {
        self.title = TDLocStrOne(@"Number");
        self.randCharacter = [NSArray arrayWithObjects:@"0", @"1", @"2", @"3", @"4", @"5", @"6", @"7", @"8", @"9", nil];
    }else if (self.randType == kRandomTypeCapital){
        self.title = TDLocStrOne(@"Capital");
        self.randCharacter = [NSArray arrayWithObjects:@"A", @"B", @"C", @"D", @"E", @"F", @"G", @"H", @"I", @"J", @"K", @"L", @"M", @"N", @"O", @"P", @"Q", @"R", @"S", @"T", @"U", @"V", @"W", @"X", @"Y", @"Z", nil];
    }else if (self.randType == kRandomTypeLowerCase){
        self.title = TDLocStrOne(@"LowerCase");
        self.randCharacter = [NSArray arrayWithObjects:@"a", @"b", @"c", @"d", @"e", @"f", @"g", @"h", @"i", @"j", @"k", @"l", @"m", @"n", @"o", @"p", @"q", @"r", @"s", @"t", @"u", @"v", @"w", @"x", @"y", @"z", nil];
    }else{
        self.title = TDLocStrOne(@"Sign");
        self.randCharacter = [NSArray arrayWithObjects:@"!", @"@", @"#", @"$", @"%", @"^", @"&", @"*", @"(", @")", @"[", @"]", @"{", @"}", @":", @";", @",", @".", @"<", @">", @"?", @"/", @"|", @"\\", nil];
    }
}
-(void)dealloc
{
    [_title release];
    [_randCharacter release];
    [super dealloc];
    
}
@end



@interface VAGenTextViewController ()<TDPickerNumberDelegate>
- (IBAction)btBackPressed:(id)sender;
@property (retain, nonatomic) IBOutlet UILabel *lbTitle;
@property (retain, nonatomic) IBOutlet UITextField *tfText;
- (IBAction)touchOnBg:(id)sender;
@property (retain, nonatomic) IBOutlet UITableView *tbConfig;
- (IBAction)generate:(id)sender;

@property(nonatomic, retain)NSMutableArray *listOption;
@property(nonatomic, retain)NSMutableArray *listCharacters;
@property(nonatomic, assign)int numText;
@property(nonatomic, assign)BOOL isDuplicatePrevention;

@end

@implementation VAGenTextViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
#define kDefaultNumText 10
- (void)viewDidLoad
{
    [super viewDidLoad];
    [TDSoundManager playShortEffectWithFile:@"chakin2.caf"];
    self.listOption = [NSMutableArray array];
    for (int i = kRandomTypeNumber; i<=kRandomTypeSign; i++) {
        [_listOption addObject:[[[TDTextRandom alloc] initWithType:i] autorelease]];
    }
    _numText = kDefaultNumText;
    _isDuplicatePrevention = YES;
    [self updateOption];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btBackPressed:(id)sender {
    [_delegate textGeneratorBack:self];
}
- (void)dealloc {
    [_lbTitle release];
    [_tfText release];
    [_tbConfig release];
    [super dealloc];
}
- (void)viewDidUnload {
    [self setLbTitle:nil];
    [self setTfText:nil];
    [self setTbConfig:nil];
    [super viewDidUnload];
}
- (IBAction)touchOnBg:(id)sender {
    [_tfText resignFirstResponder];
}
-(NSString*)genString{
    if (self.listCharacters.count == 0) {
        return @"";
    }else{
        NSString *str = @"";
        for (int i=0; i<_numText; i++) {
            NSString * s = [self.listCharacters objectAtIndex:arc4random()%_listCharacters.count];
            str = [str stringByAppendingString:s];
        }
        return str;
    }
}
-(void)updateOption{
    NSMutableArray *array = [NSMutableArray array];
    for (TDTextRandom *option in _listOption) {
        if (option.isEnable) {
            [array addObjectsFromArray:option.randCharacter];
        }
    }
    self.listCharacters = array;
}
- (IBAction)generate:(id)sender {
    self.currentText = [self genString];
    _tfText.text = self.currentText;
}

#pragma mark - tableview
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 6;
}
-(void)switchViewPressed:(UISwitch*)sw{
    if (sw.tag == _listOption.count) {
        _isDuplicatePrevention = sw.isOn;
    }else if(sw.tag < _listOption.count){
        TDTextRandom *option = [_listOption objectAtIndex:sw.tag];
        option.isEnable = sw.isOn;
        [self updateOption];
    }else{
        TDLOGERROR(@"Error tag switch %d", sw.tag);
    }
    
}
-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row <= _listOption.count ) {
        UITableViewCell *cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil] autorelease];
        [cell setAccessoryType:UITableViewCellAccessoryNone];
        
        UISwitch *sw = [[UISwitch alloc] initWithFrame:CGRectMake(230, 8, 45, 18)];
        [sw addTarget:self action:@selector(switchViewPressed:) forControlEvents:UIControlEventValueChanged];
        [cell addSubview:sw];
        sw.tag = indexPath.row;
        
        if (indexPath.row < _listOption.count) {
            TDTextRandom *randText = [_listOption objectAtIndex:indexPath.row];
            cell.textLabel.text = randText.title;
            [sw setOn: randText.isEnable];
        }else{
            cell.textLabel.text = TDLocStrOne(@"DuplicatePrevention");
            [sw setOn:_isDuplicatePrevention];
        }
        
        return cell;
    }
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:nil];
    [cell setAccessoryType:UITableViewCellAccessoryDisclosureIndicator];
    cell.textLabel.text = TDLocStrOne(@"NumCharacter");
    cell.detailTextLabel.text = [NSString stringWithFormat:@"%d", _numText];
    return cell;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row == 5) {
        [TDPickerNumber showPickerFrom:4 to:22 delegate:self];
    }
}

-(void)numberPickerdissmiss:(TDPickerNumber *)picker{
    _numText = picker.currentValue;
    [_tbConfig reloadData];
}
@end
