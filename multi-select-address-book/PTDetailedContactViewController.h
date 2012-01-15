//
//  PTDetailedContactViewController.h
//
//  Created by Ricky Cheng on 5/20/11.
//

#import <UIKit/UIKit.h>

#import <AddressBook/AddressBook.h>
#import <AddressBookUI/AddressBookUI.h>


@interface PTDetailedContactViewController : UITableViewController {
    ABRecordRef _person;
    NSMutableArray* _emailAddresses;
}

@property (nonatomic, assign) ABRecordRef person;
@property (nonatomic, retain) NSMutableArray* emailAddresses;

@end
