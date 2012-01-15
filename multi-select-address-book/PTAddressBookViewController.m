//
//  PTAddressBookViewController.m
//
//  Created by Ricky Cheng on 5/17/11.
//

#import "PTAddressBookViewController.h"
#import "PTDetailedContactViewController.h"

@implementation PTAddressBookViewController

@synthesize addressBookContacts = _addressBookContacts;
@synthesize searchBar = _searchBar;
@synthesize tableView = _tableView;
@synthesize searchDisplayController;

- (void)addContacts:(id)sender {
    // Do something here or respond to NSNotification
}

#pragma mark - View lifecycle
- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIBarButtonItem* doneButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonItemStyleDone target:self action:@selector(addContacts:)];          
    self.navigationItem.rightBarButtonItem = doneButton;
    [doneButton release];
    
    _onlyContactsWithEmail = YES;
    
    _addressBookContacts = [[NSMutableDictionary alloc] init];
    _selectedEmails      = [[NSMutableDictionary alloc] init];
    _sectionTitles       = [[NSMutableArray alloc] init];
    _filterContacts      = [[NSMutableArray alloc] init];
    _selectedContacts    = [[NSMutableArray alloc] init];
    
	ABAddressBookRef address = ABAddressBookCreate(); 
	NSArray* addressBookAry = (NSArray*)ABAddressBookCopyArrayOfAllPeopleInSourceWithSortOrdering(address, nil, kABPersonSortByFirstName);
    _peopleCount = ABAddressBookGetPersonCount(address); 

    // set section headers, A-Z and #
    for(char c = 'A'; c <= 'Z'; c++) {
        [_addressBookContacts setObject:[[NSMutableArray new] autorelease] forKey:[NSString stringWithFormat:@"%c", c]];
        [_sectionTitles addObject:[NSString stringWithFormat:@"%c",c]];
    }
    [_addressBookContacts setObject:[NSMutableArray new] forKey:@"#"];
    [_sectionTitles addObject:@"#"];

    NSInteger tmpPersonCount = 0;
    // loop through all the people and append it under the appropriate section headers
    for (int i = 0; i < _peopleCount; i++) {
        BOOL canAppendToAddressBook = YES;
        
        ABRecordRef person = [addressBookAry objectAtIndex:i];
        if (_onlyContactsWithEmail) {
            ABMultiValueRef emails = ABRecordCopyValue(person, kABPersonEmailProperty);
            NSInteger emailCount = ABMultiValueGetCount(emails);
            canAppendToAddressBook = (emailCount > 0);
            CFRelease(emails);
        }
        
        if (canAppendToAddressBook) {
            NSString* firstName    = (NSString*)ABRecordCopyValue(person, kABPersonFirstNameProperty); 
            NSString* middleName   = (NSString*)ABRecordCopyValue(person, kABPersonMiddleNameProperty); 
            NSString* lastName     = (NSString*)ABRecordCopyValue(person, kABPersonLastNameProperty);
            NSString* organization = (NSString*)ABRecordCopyValue(person, kABPersonOrganizationProperty);
            
            tmpPersonCount += 1;
            
            NSString *name;
            if      (firstName)    { name = firstName;    }
            else if (middleName)   { name = middleName;   }
            else if (lastName)     { name = lastName;     }
            else if (organization) { name = organization; }
                    
            if (name) {
                NSString* key = [[name substringToIndex:1] capitalizedString];
                NSMutableArray* tempAddresBook = [_addressBookContacts objectForKey:key];
                
                if (tempAddresBook) {
                    [tempAddresBook addObject:person];
                } else {
                    NSMutableArray* tempSymAddresBook = [_addressBookContacts objectForKey:@"#"];
                    [tempSymAddresBook addObject:person];
                }
            }
        }
    }
    
    if (_onlyContactsWithEmail) { _peopleCount = tmpPersonCount; }
    
    UILabel* footerView = [[UILabel alloc] initWithFrame:CGRectMake(0,0,320,24)];
    footerView.text = [NSString stringWithFormat:@"%i Contacts", _peopleCount];
    footerView.textAlignment = UITextAlignmentCenter;
    footerView.font = [UIFont systemFontOfSize:20.0f];
    footerView.textColor = [UIColor darkGrayColor];
    self.tableView.tableFooterView = footerView;
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return (tableView == searchDisplayController.searchResultsTableView) ? 1 : [_addressBookContacts count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (tableView == searchDisplayController.searchResultsTableView) {
        return [_filterContacts count];
    }
    
    return [[_addressBookContacts objectForKey:[_sectionTitles objectAtIndex:section]] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }

    NSMutableArray* ary;
    if (tableView == searchDisplayController.searchResultsTableView) {
        ary = _filterContacts;
    } else {
        ary = [_addressBookContacts objectForKey:[_sectionTitles objectAtIndex:indexPath.section]];
    }
    
    ABRecordRef person = [ary objectAtIndex:indexPath.row];
    cell.textLabel.text = [self getTitleForContact:person];
    cell.accessoryType = [_selectedContacts containsObject:person] ? UITableViewCellAccessoryCheckmark : UITableViewCellAccessoryNone;
    
    return cell;
}

- (NSString*)getTitleForContact:(ABRecordRef)person {
    NSString *result;
    NSString *firstName    = (NSString *)ABRecordCopyValue(person, kABPersonFirstNameProperty); 
    NSString *middleName   = (NSString *)ABRecordCopyValue(person, kABPersonMiddleNameProperty); 
    NSString *lastName     = (NSString *)ABRecordCopyValue(person, kABPersonLastNameProperty); 
    NSString* organization = (NSString*)ABRecordCopyValue(person, kABPersonOrganizationProperty);
    
    NSMutableArray* nameAry = [[NSMutableArray alloc] init];
    if (firstName)  { [nameAry addObject:firstName];  }
    if (middleName) { [nameAry addObject:middleName]; }
    if (lastName)   { [nameAry addObject:lastName];   }
    
    [firstName release];
    [middleName release];
    [lastName release];

    NSString* fullname = [nameAry componentsJoinedByString:@" "];
    [nameAry release];
    
    if (![fullname isEqualToString:@""]) {
        result =  fullname;
    } else if (organization) {
        result = organization;
    } else {
        ABMultiValueRef emails = ABRecordCopyValue(person, kABPersonEmailProperty);
        CFStringRef email = ABMultiValueCopyValueAtIndex(emails, 0);
        result = (NSString*)email;
        CFRelease(email);
        CFRelease(emails);
    }
    [organization release];

    return result;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return [_sectionTitles objectAtIndex:section];
}

- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView {
    return _sectionTitles;
}

#pragma mark - Table view delegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    UITableViewCell* cell = [tableView cellForRowAtIndexPath:indexPath];
    
    NSMutableArray* ary;
    if (tableView == searchDisplayController.searchResultsTableView) {
        ary = _filterContacts;
    } else {
        ary = [_addressBookContacts objectForKey:[_sectionTitles objectAtIndex:indexPath.section]];
    }
    
    ABRecordRef person = [ary objectAtIndex:indexPath.row];

    ABMultiValueRef emails = ABRecordCopyValue(person, kABPersonEmailProperty);
    NSInteger emailCount = ABMultiValueGetCount(emails);
    
    if (emailCount > 1) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:@"MultipleEmailSelectNotification" object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(addSelectedUserToList:) name:@"MultipleEmailSelectNotification" object:nil];
        PTDetailedContactViewController* detailedContactViewController = [[PTDetailedContactViewController alloc] initWithNibName:@"PTDetailedContactViewController" bundle:[NSBundle mainBundle]];
        detailedContactViewController.person = person;
        [self.navigationController pushViewController:detailedContactViewController animated:YES];
        [detailedContactViewController release];
    } else {
        if (cell.accessoryType == UITableViewCellAccessoryCheckmark) {
            cell.accessoryType = UITableViewCellAccessoryNone;
            [_selectedContacts removeObject:person];
            [_selectedEmails removeObjectForKey:person];
        } else {
            cell.accessoryType = UITableViewCellAccessoryCheckmark;
            [_selectedContacts addObject:person];
            CFStringRef email = ABMultiValueCopyValueAtIndex(emails, 0);
//            [_selectedEmails setObject:(NSString*)email forKey:person];
            CFRelease(email);
        }
    }
    
    CFRelease(emails);
}

- (void)addSelectedUserToList:(NSNotification*)notification {
    NSString* email = [[notification userInfo] valueForKey:@"email"];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"MultipleEmailSelectNotification" object:nil];
}

#pragma mark -
#pragma mark Content Filtering
- (void)filterContentForSearchText:(NSString*)searchText scope:(NSString*)scope {
    [_filterContacts removeAllObjects];
	
	for (NSString *key in _sectionTitles) {
        NSArray* people = [_addressBookContacts objectForKey:key];
        for (int i=0;i<[people count];i++) {
            ABRecordRef person = [people objectAtIndex:i];
            NSString* title = [self getTitleForContact:person];
            
            NSRange result = [title rangeOfString:searchText options:(NSCaseInsensitiveSearch|NSDiacriticInsensitiveSearch)];

            if(result.location != NSNotFound) {
                [_filterContacts addObject:person];
            }
        }
    }
}


#pragma mark -
#pragma mark UISearchDisplayController Delegate Methods
- (BOOL)searchDisplayController:(UISearchDisplayController *)controller shouldReloadTableForSearchString:(NSString *)searchString {
    [self filterContentForSearchText:searchString scope:[[_searchBar scopeButtonTitles] objectAtIndex:[_searchBar selectedScopeButtonIndex]]];
    return YES;
}

#pragma mark-
- (void)dealloc {
    [_addressBookContacts release], _addressBookContacts = nil;
    [_searchBar release], _searchBar = nil;
    [_tableView release], _tableView = nil;
    [searchDisplayController release], searchDisplayController = nil;
    [super dealloc];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

@end
