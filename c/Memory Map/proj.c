// Ricky Cheng
// February 2008
// Two Pass Linker
// Comments: 
// After copying and pasting text from the web and saving the contents
// into a text file, a file parsing error sometimes occur. If there is a trailing
// enter delete it and place it back or remove it altogther and it should work.

#include <stdio.h>
#include <string.h>
#define MAX_MODULES 100
#define MEMORY_MAP 256
#define FILE_SIZE 10000
#define NAME_SIZE 20

// declare functions
int readFile();
void createModules();
void printString(char *ary);
void printSymbols();
void printOffsets();
void toString();
void printMemoryMap();
void printUnusedSymbols();
void printUnusedModuleSymbols();
int parseInt(int val);
int getSymbolValue(char *name);

// declare global variables
int numModules = 0;
int totalSymbols = 0;
char fileName[50];
char fileData[FILE_SIZE];
struct node *modulesList;
struct node *symbolsList;

// define structures
/** Modules Class */
struct module {
  char name[NAME_SIZE];
  int numSymbols;
  int numAddresses;
  struct node *symbols;
  struct node *memoryMap;
};

/** Symbols Class */
struct symbol {
  char name[NAME_SIZE];
  int value;
  int usedBool; 
};

/** Node Class, Linked List */
struct node {
  struct symbol *symbolObject;
  struct module *moduleObject;
  struct node *next;
};

/** This is the main method. */
int main() {
  // reads the file, if the file exists then break the data
  // up into modules
  if(readFile()) {
    createModules();
    printSymbols();
    printOffsets();
    printMemoryMap();
    printUnusedSymbols();
    printUnusedModuleSymbols();
  }
}

/** This is the main method in creating the modules. It loops through
 *  the fileData array and puts everything accordingly into the structure. 
 */
void createModules() {
  // the index of the data from the text file
  int i=0;

  // temporary string 
  char str[NAME_SIZE];

  // string index
  int strIndex=0;

  // temporary address 
  int tempAddress = 0;

  // this boolean is used to check for blank spaces and new lines
  // when it is on it tells the string to keep copying characters
  // until another space is hit
  int startBool = 0;

  // these booleans are used to check which part of the string 
  // manipulating the function is up to
  int moduleBool = 0;
  int symbolBool = 0;
  int symbolListBool = 0;
  int mapBool = 0;

  // number of symbols to declare
  int numSymbolsDeclare = -1;
  int numSymbolsUsedDeclare = -1;
  int numAddressesDeclare = -1;

  // this bool is used to check whether the current string is
  // an instance of a value or the symbol name
  int symbolNameValueBool = 0;

  // indexes
  int currentModuleIndex = 0;
  int currentSymbolIndex = 0;
  int currentUsedSymbolIndex = 0;
  int currentMapIndex = 0;

  // declaration for modules list 
  struct node tempModuleNode[MAX_MODULES];
  struct node *currentNode = &tempModuleNode[currentModuleIndex];
  modulesList = currentNode;
  struct module tempModule[MAX_MODULES];
  currentNode->moduleObject = &tempModule[currentModuleIndex];
  struct module *currentModule = currentNode->moduleObject;

  // this is for the used symbols in the modules
  struct node tempUsedNode[100];
  struct node *currentUsedSymbolNode = &tempUsedNode[currentUsedSymbolIndex];
  currentModule->symbols = currentUsedSymbolNode; 
  struct symbol tempUsedSymbol[100];
  currentUsedSymbolNode->symbolObject = &tempUsedSymbol[currentUsedSymbolIndex];
  struct symbol *currentUsedSymbol = currentUsedSymbolNode->symbolObject;

  // declaration for memory map
  struct node tempMapNode[100];
  struct node *currentMapNode = &tempMapNode[currentMapIndex];
  currentModule->memoryMap = currentMapNode;
  struct symbol tempAddresses[MEMORY_MAP];
  currentMapNode->symbolObject = &tempAddresses[currentMapIndex];
  struct symbol *currentAddress = currentMapNode->symbolObject;

  // declaration for symbols list
  struct node tempSymbolNode[50];
  struct node *currentSymbolNode = &tempSymbolNode[currentSymbolIndex];
  symbolsList = currentSymbolNode;
  struct symbol tempSymbol[50];
  currentSymbolNode->symbolObject = &tempSymbol[currentSymbolIndex];
  struct symbol *currentSymbol = currentSymbolNode->symbolObject;

  // loops through the fileData character array until it hits the & which
  // signifies that it is the end of the array
  while(fileData[i] != '&') {
    // first converts all the return carriages and new lines to spaces
    if(fileData[i] == '\n' || fileData[i] == '\r')
      fileData[i] = ' ';

    // checks for the first non white space, if it is detected it is telling
    // the temporary string to start recording characters until it hits a blank
    // character again
    if(fileData[i] != ' ') 
      startBool = 1;

    // if startBool is true and next character in fileData array is a space then
    // turn the bool to false and place a stopper at the end of the array, resets 
    // the temporary string index
    if(startBool == 1 && fileData[i] == ' ') {
      startBool = 0;
      str[strIndex] = '&';
      strIndex = 0;

      // step H - if everything is complete it moves on to the next module
      if(mapBool == 1) {
        // resets all the bools and counters, leaves the indexes in place because
        // it will continue from that point
        moduleBool = symbolBool = symbolListBool = mapBool = 0;	
        numSymbolsDeclare = numSymbolsUsedDeclare = numAddressesDeclare = -1;
        symbolNameValueBool = 0;

        // increments the module index
        currentModuleIndex++;
        currentNode->next = &tempModuleNode[currentModuleIndex];	
        currentNode = currentNode->next;
        currentNode->moduleObject = &tempModule[currentModuleIndex];
        currentModule = currentNode->moduleObject;

        currentMapIndex++;
        currentMapNode = &tempMapNode[currentMapIndex];
        currentModule->memoryMap = currentMapNode;
        currentMapNode->symbolObject = &tempAddresses[currentMapIndex];
        currentAddress = currentMapNode->symbolObject;

        currentUsedSymbolIndex++;
        currentUsedSymbolNode = &tempUsedNode[currentUsedSymbolIndex];
        currentModule->symbols = currentUsedSymbolNode;
        currentUsedSymbolNode->symbolObject = &tempUsedSymbol[currentUsedSymbolIndex];
        currentUsedSymbol = currentUsedSymbolNode->symbolObject;
      }

      // step G - creates memory map
      if(mapBool == 0 && symbolListBool == 1 && numAddressesDeclare != -1) {
        if(numAddressesDeclare != 0) {
          if(symbolNameValueBool == 0) {
            // stores the opcode
            strcpy(currentAddress->name,&str[0]);
            symbolNameValueBool = 1;
          } else {
            // stores the address and then increments until there are
            // no more addresses
            currentAddress->value = atoi(str);
            numAddressesDeclare--;
            symbolNameValueBool = 0;
            currentMapIndex++;
            currentMapNode->next = &tempMapNode[currentMapIndex];
            currentMapNode = currentMapNode->next;
            currentMapNode->symbolObject = &tempAddresses[currentMapIndex];
            currentAddress = currentMapNode->symbolObject;
          }
        }

        if(numAddressesDeclare == 0)
          mapBool = 1;
      }

      // step F - gets the number of memory addresses
      if(mapBool == 0 && symbolListBool == 1) {
        if(numAddressesDeclare == -1) {
          numAddressesDeclare = atoi(str);
          currentModule->numAddresses = numAddressesDeclare;
          tempAddress += currentModule->numAddresses;

          // resets the symbol name and value bool
          symbolNameValueBool = 0;
        }
      }

      // step E - list of symbols used in this module
      if(symbolListBool == 0 && symbolBool == 1 && numSymbolsUsedDeclare != -1) {
        if(numSymbolsUsedDeclare != 0) {
          currentUsedSymbolIndex++;
          strcpy(currentUsedSymbol->name,&str[0]);
          currentUsedSymbolNode->next = &tempUsedNode[currentUsedSymbolIndex];
          currentUsedSymbolNode = currentUsedSymbolNode->next;
          currentUsedSymbolNode->symbolObject = &tempUsedSymbol[currentUsedSymbolIndex];
          currentUsedSymbol = currentUsedSymbolNode->symbolObject;
          numSymbolsUsedDeclare--;
        }

        if(numSymbolsUsedDeclare == 0)
          symbolListBool = 1;
      }

      // step D - gets the number of symbols to be used
      if(symbolListBool == 0 && symbolBool == 1) 
        if(numSymbolsUsedDeclare == -1) {
          numSymbolsUsedDeclare = atoi(str);
          currentModule->numSymbols = numSymbolsUsedDeclare;

          // first check to see if nothing exists 
          if(numSymbolsUsedDeclare == 0)
            symbolListBool = 1;
        }

      // step C - grabs all the symbols and assigns the name / value
      if(numSymbolsDeclare != -1 && symbolBool == 0) {
        // if there are no symbols to declare then skip this step
        if(numSymbolsDeclare != 0) {
          // if symbolsNameValueBool is equal to 0 then it means
          // the name of the symbol has to be stored, if the value is 
          // equal to 1 that means it is the value of the symbol
          if(symbolNameValueBool == 0) {
            strcpy(currentSymbol->name, &str[0]); 
            if(getSymbolValue(currentSymbol->name) == -1) {
              totalSymbols++;
              symbolNameValueBool = 1;
            } else {
              // if a symbol has already been defined print out an error message
              printf("Error: Symbol ");
              toString(currentSymbol->name); 
              printf(" has been defined already.\n");
              numSymbolsDeclare--;
              i++;
            }
          } else if(symbolNameValueBool == 1) {
            // after the value of the symbol has been set does the 
            // number of symbols get decremented
            currentSymbol->value = tempAddress + atoi(str); 
            numSymbolsDeclare--;
            symbolNameValueBool = 0;
            currentSymbolIndex++;
            currentSymbolNode->next = &tempSymbolNode[currentSymbolIndex];
            currentSymbolNode = currentSymbolNode->next;
            currentSymbolNode->symbolObject = &tempSymbol[currentSymbolIndex];
            currentSymbol = currentSymbolNode->symbolObject;
          }
        }

        // if there are no symbols start the next step
        if(numSymbolsDeclare == 0) 
          symbolBool = 1;
      }

      // step B - finds out the total amount of symbols there are in the module
      if(moduleBool == 1 && numSymbolsDeclare == -1) {
        numSymbolsDeclare = atoi(str);	

        if(numSymbolsDeclare == 0)
          symbolBool = 1;
      }

      // step A - gets the name of the module
      if(moduleBool == 0) {
        moduleBool = 1;
        numModules++;
        char *namePointer = currentModule->name;
        char *strPointer = &str[0];
        strcpy(namePointer,strPointer);
      }
    }

    // if startBool is on then add the character onto the string and increment
    if(startBool == 1) {
      str[strIndex] = fileData[i];
      strIndex++;
    }

    // increment counter for the while loop
    i++;
  }
}

/** This method prints out the memory map. */
void printMemoryMap() {
  struct node* iterator = modulesList;
  int i;
  int offsetValue = 0;
  int memoryLocCounter = 0;

  printf("\nMemory Map\n");
  // loops through the modules and pulls out the memory map
  // for each module and manipulates the data to print out the map
  for(i=0;i<numModules;i++) {
    struct module *obj = iterator->moduleObject;
    iterator = iterator->next;

    // temporary counter for the 2nd loop, this loop goes through
    // the memory address map and prints it to the screen
    int j;
    struct node *mapIterator = obj->memoryMap;
    for(j=0;j<obj->numAddresses;j++) {
      struct node *symbolIterator = obj->symbols;
      // iterator to loop through
      struct symbol mapObj = *mapIterator->symbolObject;
      mapIterator = mapIterator->next;

      // actually parsing begins here
      char opcode = *mapObj.name;
      int newValue = mapObj.value;

      // a set of booleans used to print out warnings, warningBool is used to 
      // signify if there was a warning, external address > length ; absoluateWarn
      // is enabled when the last 3 digits are > 249 ; undefinedWarnBool is on when
      // a symbol is referenced by not defined ; relative address exceeds module size
      int warningBool = 0;
      int absoluteWarnBool = 0;
      int undefinedWarnBool = 0;
      int moduleSizeWarnBool = 0;

      // depending on the opcode, the address gets manipulated
      if(opcode == 'R') {
        newValue += offsetValue;
        int address = parseInt(newValue);
        if(address > 249) {
          newValue = newValue - address;
          moduleSizeWarnBool = 1;
        }
      }
      else if(opcode == 'A') {
        int address = parseInt(newValue);
        // max address size
        if(address > 249) {
          newValue = newValue - address;
          absoluteWarnBool = 1;
        }
      } else if(opcode == 'E') { 
        // this parses the integer and gets the last digit to see what 
        // symbol it is using
        int symbolsLoc = parseInt(newValue);

        // a third loop to parse through the symbols declared table
        int w;

        // prints out a warning if the location exceeds the number of symbols
        if(symbolsLoc > obj->numSymbols || symbolsLoc > totalSymbols) {
          warningBool = 1;
        } else { 
          for(w=0;w<symbolsLoc;w++) 
            symbolIterator = symbolIterator->next;
          struct symbol *usedSymbolObj = symbolIterator->symbolObject;
          usedSymbolObj->usedBool = 1;
          // replaces the last two digits of the address with this number
          int newSymbolLoc = getSymbolValue(usedSymbolObj->name);
          if(newSymbolLoc == -1) {
            newSymbolLoc = 0;
            undefinedWarnBool = 1;
          }
          newValue = newValue - symbolsLoc + newSymbolLoc; 
        }
      }
      printf("%d: %c - %d ",memoryLocCounter, opcode, newValue);

      if(warningBool == 1) 
        printf("[Error: External Address Exceeds Length, Treated As Immediate]"); 
      else if(absoluteWarnBool == 1)
        printf("[Error: Absolute Address Exceeds Machine Size, Zero Used]");
      else if(undefinedWarnBool == 1)
        printf("[Error: Symbol Used Not Defined, Zero Used]");
      else if(moduleSizeWarnBool == 1)
        printf("[Error: Relative Address Exceeds Module Size; Zero Used]");

      printf("\n");
      // this is the main counter
      memoryLocCounter++;
    }
    offsetValue += obj->numAddresses;
  }
}

/** This method iterates through the symbols list and checks if there is
 *  a match with the parmeter symbol name. If there is then it returns the
 *  appropriate value. Otherwise it returns a 0.
 */
int getSymbolValue(char *name) {
  struct node *iterator = symbolsList;
  int i=0;
  while(i<totalSymbols) {
    struct symbol *obj = iterator->symbolObject;
    iterator = iterator->next;

    int j=0;
    int matchBool = 1;
    // loops until it hits the last character, terminating character = &
    while(name[j] != '&') {
      if(name[j] != obj->name[j])
        matchBool = 0;
      j++;
    }
    if(matchBool == 1) {
      // triggers the symbol used bool
      obj->usedBool = 1;
      return obj->value;
    }
    i++;
  }
  return -1;
}

/** This method iterates through the symbols list and prints out the symbols that
 *  are defined but not used.
 */
void printUnusedSymbols() {
  struct node *iterator = symbolsList; 
  int i;

  printf("\n");
  for(i=0;i<totalSymbols;i++) {
    struct symbol obj = *iterator->symbolObject;
    iterator = iterator->next;
    if(obj.usedBool == 0) {
      printf("Warning: ");
      toString(obj.name);
      printf(" was defined, but it was never used.\n");
    }
  }
}

/** This method prints out the symbols and their values. */
void printSymbols() {
  struct node *iterator = symbolsList; 
  int i;
  for(i=0;i<totalSymbols;i++) {
    struct symbol obj = *iterator->symbolObject;
    iterator = iterator->next;
    printf("[SYMBOL: ");
    toString(obj.name);
    printf(" | VALUE: %d]\n", obj.value);
  }
}

/** This method prints out the offsets. */
void printOffsets() {
  printf("\nOffsets\n");
  struct node *iterator = modulesList;
  int i=0;
  int offsetValue = 0;

  for(i=0;i<numModules;i++) {
    struct module obj = *iterator->moduleObject;
    iterator = iterator->next;
    printf("[%d | ",i+1);
    toString(obj.name);
    printf(" | OFFSET: %d]\n", offsetValue); 
    offsetValue += obj.numAddresses;
  }
}

/** -- TEST METHOD | CAN BE DELETED --
 *  This method prints out the string, terminating character is '&'. 
 */
void printString(char *ary) {
  int i=0;
  printf("[");
  while(ary[i] != '&') {
    printf("%c",ary[i]);
    i++;   
  } 
  printf("]\n");
}

/** This method prints out a character array. */
void toString(char *ary) {
  int i=0;
  while(ary[i] != '&') {
    printf("%c",ary[i]);
    i++;   
  } 
}

/** This method goes through each module and loops through the defined symbols
 *  list and prints out each symbol that is defined but not used. 
 */
void printUnusedModuleSymbols() {
  struct node* iterator = modulesList;
  int i;

  for(i=0;i<numModules;i++) {
    struct module *obj = iterator->moduleObject;
    iterator = iterator->next;

    int j;
    struct node *symbolIterator = obj->symbols;
    for(j=0;j<obj->numSymbols;j++) {
      struct symbol symObj = *symbolIterator->symbolObject;
      symbolIterator = symbolIterator->next;
      if(symObj.usedBool == 0) {
        printf("Warning: ");
        toString(symObj.name);
        printf(" was defined in module ");
        toString(obj->name);
        printf(" but it was never used.\n");
      }
    }
  }
}

/** This method parses the address value. Returns last 3 digits. */
int parseInt(int val) {
  while(val > 999) 
    val = val%1000;
  return val;
}

/** This method reads in the file into fileData array. */
int readFile() {
  // askes the user what the file name is
  printf("What is the file name? ");
  scanf("%s",fileName);

  // checks if the file name exists
  FILE *textFile;
  textFile = fopen(fileName,"r");
  if(textFile == NULL) {
    printf("This file does not exist.\n");
    return 0;
  }
  else {
    // if the file exists then read each character into
    // the fileData array
    int i=0;
    while(!feof(textFile)) {
      fscanf(textFile,"%c",&fileData[i]);
      i++;
    }
    // simple hack to make it so that files with or without a \n
    // still parse
    if(fileData[i-2] != '\n') {
      fileData[i] = '\n';
      i++;
    }
    fileData[i] = '&';
    fclose(textFile);
    printf("\n");
    return 1;
  }
}
