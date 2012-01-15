//
//  PTAddressBookViewController.h
//
//  Created by Ricky Cheng on 5/17/11.
//

#import <UIKit/UIKit.h>

#import <AddressBook/AddressBook.h>
#import <AddressBookUI/AddressBookUI.h>

@interface PTAddressBookViewController : UIViewController <UISearchBarDelegate> {
    NSMutableDictionary* _addressBookContacts;
    NSMutableArray* _sectionTitles;
    NSMutableArray* _filterContacts;
    
    NSMutableArray* _selectedContacts;
    NSHashTable* _selectedEmails;
    
    UISearchBar* _searchBar;
    UITableView* _tableView;
    
    UISearchDisplayController* searchDisplayController;
    
    NSInteger _peopleCount;
    BOOL _onlyContactsWithEmail;
}

@property (nonatomic, retain) NSMutableDictionary* addressBookContacts;
@property (nonatomic, retain) IBOutlet UISearchDisplayController* searchDisplayController;
@property (nonatomic, retain) IBOutlet UISearchBar* searchBar;
@property (nonatomic, retain) IBOutlet UITableView* tableView;

- (NSString*)getTitleForContact:(ABRecordRef)person;

@end
