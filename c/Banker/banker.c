// Ricky Cheng
// March 2008
// Bankers

#include <stdio.h>
#include <string.h>
#define FILE_SIZE 10000
#define ACTIVITY_SIZE 10000
#define INITIATE 1
#define REQUEST 2
#define RELEASE 3
#define TERMINATE 4

// function declarations
int readFile(char file[]);
void parseDataFile();
int activityToInt(char *str);
void runFIFO();
void runBanker();
void printStatistics();
void resetAll();
void findStartLocations();
void findNextLocationForTask(int task);

// global variables
int numTasks = 0;
int numResourcesFinal = 0;
int resourceUnits[50];
int resourceUnitsRemaining[50];
int numActivities = 0;
char * fileName;
char fileData[FILE_SIZE];

// define main variables
int taskNumber[ACTIVITY_SIZE];
int delay[ACTIVITY_SIZE];
int activity[ACTIVITY_SIZE];
int resourceType[ACTIVITY_SIZE];
int numResourceUnits[ACTIVITY_SIZE];

// define queues
int terminateCycle[50];
int totalBlocks[50];

// if set to 0 means ignore
int blockedQueue[50];
int readyQueue[50];
int numberBlocked = 0;
// change and swap to -1 when invalid ^^^

// number of variables that have been terminated
int numberTerminated = 0;

// define task variables
int activityCurrentIndex[ACTIVITY_SIZE];

// total resources allowed declaration, ARRAY[task number][resource number] = value
int resourcesForTask[50][ACTIVITY_SIZE];

// amount of resource units currently requesting PER TASK
int resourcesRequested[50][ACTIVITY_SIZE];

int main(int argc, char * argv[]) {
  readFile(argv[1]);
  parseDataFile();

  findStartLocations();
  runFIFO();
  printf("FIFO\n");
  printStatistics();

  resetAll();

  parseDataFile();
  findStartLocations();
  runBanker();
  printf("\nBANKER\n");
  printStatistics();
}

void resetAll() {
  numberTerminated = 0;
  numberBlocked = 0;

  int i, j;
  for (i = 0; i < 50; i++) {
    terminateCycle[i] = 0;
    totalBlocks[i] = 0;
    readyQueue[i] = 0;
    blockedQueue[i] = 0;
    activityCurrentIndex[0];

    for (j = 0; j < 50; j++) {
      resourcesForTask[i][j] = 0;
      resourcesRequested[i][j] = 0;
    }
  }

}

/**
 *
 */
void printStatistics() {
  int i, tempCycleTotal = 0, tempBlockTotal = 0;

  for (i = 0; i < numTasks; i++) {
    if (terminateCycle[i] != -1) {
      tempCycleTotal += terminateCycle[i];
      tempBlockTotal += totalBlocks[i];
      int percent = (totalBlocks[i]*100/terminateCycle[i]);
      printf("TASK #%i - TERMINATE CYCLE: %i | BLOCKS: %i | %i%%\n", i+1, terminateCycle[i], totalBlocks[i], percent);
    } else
      printf("TASK #%i - ABORED\n", i+1);
  }

  int percent = (tempBlockTotal*100/tempCycleTotal);
  printf("TOTAL - CYCLES: %i | BLOCKS: %i | %i%%\n",tempCycleTotal,tempBlockTotal,percent); 
}

/**
 * This function locates the starting index of the activity array.
 */
void findStartLocations() {
  int i, j;

  for (i = 0; i < numTasks; i++) 
    for (j = 0; j < numActivities; j++) 
      // add one because the array starts at 0, while the task actually
      // starts at 1
      if ((i+1) == taskNumber[j]) {
        activityCurrentIndex[i] = j;
        break;
      }
}

/**
 *
 */
void findNextLocationForTask(int task) {
  int i;
  for (i = activityCurrentIndex[task]+1; i < numActivities; i++) {
    if (taskNumber[i] == task+1) {
      activityCurrentIndex[task] = i;
      break;
    }
  }
}

void runBanker() {
  int cycle = 0;
  int blockIndex, readyIndex;
  int deadlockBool = 0;
  while (numberTerminated != numTasks) {
    int i;
    // =D
    int skipAry[50];
    for (i = 0; i < numTasks; i++)
      skipAry[i] = 0;
    // ---- end =D 

    // <!-- start, the temporary amount of resources remaining
    int tempResourcesRemaining[50];
    for (i = 0; i < numResourcesFinal; i++) 
      tempResourcesRemaining[i] = resourceUnitsRemaining[i];
    // end -->

    // first check if any blocked and grab lowest blocked task
    int lowestBlockedIndex = 0;
    for (blockIndex = 0; blockIndex < numberBlocked; blockIndex++) {
      int taskNumber = blockedQueue[blockIndex];

      // retrieves the lowest blocked task incase of a deadlock
      if (taskNumber < blockedQueue[lowestBlockedIndex] && taskNumber != -1)
        lowestBlockedIndex = blockIndex;

      // meaning if not null 
      if (taskNumber != -1) {
        if (deadlockBool == 0)
          totalBlocks[taskNumber]++;
        // check if able to grant request
        int activityIndex = activityCurrentIndex[taskNumber];

        int requestResourceNum = resourceType[activityIndex]-1;
        int numResources = numResourceUnits[activityIndex];

        // if the resource unit has more resources then being requested then continue
        if (tempResourcesRemaining[requestResourceNum] >= numResources) {
          // this is a personal checker, checks if amount of resources requested 
          // is < or = to the amount allowed
          if (resourcesForTask[taskNumber][requestResourceNum] >= numResources) {
            // bankers checker
            int bankerBool = 1;
            int bankerI;

            for (bankerI = 0; bankerI < numTasks; bankerI++) {
              if (bankerI != taskNumber && terminateCycle[bankerI] == 0) {
                int secBankerBool = 0;
                int bankerJ;

                for (bankerJ = 0; bankerJ < numResourcesFinal; bankerJ++) 
                  if (resourcesRequested[bankerI][bankerJ] != 0) 
                    secBankerBool = 1;

                if (secBankerBool == 1) {
                  int bankerK;
                  for (bankerK = 0; bankerK < numResourcesFinal; bankerK++) {
                    int totalUnitsNeeded = resourcesForTask[bankerI][bankerK]-resourcesRequested[bankerI][bankerK];
                    if (totalUnitsNeeded >= tempResourcesRemaining[bankerK]-numResources) 
                      bankerBool = 0;

                  }
                  // end multi
                }
              }
            }

            if (bankerBool == 1) {
              if (resourceUnitsRemaining[requestResourceNum] >= numResources) {
                // decrement total remaining
                tempResourcesRemaining[requestResourceNum] -= numResources;

                // assign the resources
                resourcesRequested[taskNumber][requestResourceNum] += numResources;
                findNextLocationForTask(taskNumber);
                blockedQueue[blockIndex] = -1;
                readyQueue[taskNumber] = 1;
                deadlockBool = 0;
                skipAry[taskNumber] = 1;
              } 
            }
          } 
        }
      }
    }

    // deadlocked
    if (numberBlocked == numTasks-numberTerminated) {
      int lowestBlockedTask = blockedQueue[lowestBlockedIndex];

      if (lowestBlockedTask != -1) {
        // remove lowest task number
        // loop through each blocked and grab lowest
        numberTerminated++;

        // releases the resource units
        for (i = 0; i < numResourcesFinal; i++) {
          int numResources = resourcesRequested[lowestBlockedTask][i];
          tempResourcesRemaining[i] += numResources;
        }
        terminateCycle[lowestBlockedTask] = -1;
        blockedQueue[lowestBlockedIndex] = -1;
        deadlockBool++;
      }
    }

    // ?
    int tempNumBlocked = 0;
    for (i = 0; i < numberBlocked; i++) {
      if (blockedQueue[i] == -1) {
        int j;
        for (j = i; j < numberBlocked; j++) 
          blockedQueue[j] = blockedQueue[j+1];
        tempNumBlocked++;
      }
    }
    numberBlocked -= tempNumBlocked;
    // ?

    if (deadlockBool == 0)
      for (readyIndex = 0; readyIndex < numTasks; readyIndex++) {
        // the array's task number location
        if (readyQueue[readyIndex] == 1 && skipAry[readyIndex] != 1) {
          int taskNumber = readyIndex;

          // grab activityIndex
          int activityIndex = activityCurrentIndex[taskNumber];

          // grab activity
          int activ = activity[activityIndex];
          if (activ == INITIATE) {
            int requestResourceNum = resourceType[activityIndex]-1;
            int numResources = numResourceUnits[activityIndex];

            if (numResources > resourceUnits[requestResourceNum]) {
              numberTerminated++;
              terminateCycle[taskNumber] = -1;
              activityCurrentIndex[taskNumber] = -1;
            } else {
              resourcesForTask[taskNumber][requestResourceNum] = numResources;
              findNextLocationForTask(taskNumber);
            }
          } else if (activ == REQUEST) {
            // delay counter
            int delayTime = delay[activityIndex];

            if (delayTime == 0) {		
              // check if there is enough, if there is subtract and add
              int requestResourceNum = resourceType[activityIndex]-1;
              int numResources = numResourceUnits[activityIndex];

              // if the amount of resources avaliable is > or = then the amount being requested
              if (tempResourcesRemaining[requestResourceNum] >= numResources) {
                // check if amount of resources requested < or = to the amount allowed
                if (resourcesForTask[taskNumber][requestResourceNum] >= numResources) {
                  // bankers checker
                  int bankerBool = 1;
                  int bankerI;

                  for (bankerI=0;bankerI<numTasks;bankerI++) {
                    if (bankerI != taskNumber && terminateCycle[bankerI] == 0) {
                      int secBankerBool = 0;
                      int bankerJ;

                      for (bankerJ=0;bankerJ<numResourcesFinal;bankerJ++) {
                        if (resourcesRequested[bankerI][bankerJ] != 0) 
                          secBankerBool = 1;
                      }

                      if (secBankerBool == 1) {
                        // amount of units each task needs to complete itself
                        int totalUnitsNeeded = resourcesForTask[bankerI][requestResourceNum]-resourcesRequested[bankerI][requestResourceNum];

                        if (totalUnitsNeeded > tempResourcesRemaining[requestResourceNum]-numResources) 
                          bankerBool = 0;
                      }
                    }
                  }

                  if (bankerBool == 1) {
                    if (resourceUnitsRemaining[requestResourceNum] >= numResources) {
                      // if requesting over the amount of resources allowed, terminate
                      if (numResources+resourcesRequested[taskNumber][requestResourceNum] > resourcesForTask[taskNumber][requestResourceNum]) {
                        numberTerminated++;

                        // releases the resource units
                        for (i = 0; i < numResourcesFinal; i++)
                          tempResourcesRemaining[i] += resourcesRequested[taskNumber][i];

                        terminateCycle[taskNumber] = -1;
                        activityCurrentIndex[taskNumber] = -1;
                      } else {
                        // decrement total remaining
                        tempResourcesRemaining[requestResourceNum] -= numResources;

                        // assign the resources
                        resourcesRequested[taskNumber][requestResourceNum] += numResources;
                        findNextLocationForTask(taskNumber);
                      }
                    } else {
                      readyQueue[taskNumber] = 0;
                      blockedQueue[numberBlocked] = taskNumber;
                      numberBlocked++;
                    }
                  } else {
                    readyQueue[taskNumber] = 0;
                    blockedQueue[numberBlocked] = taskNumber;
                    numberBlocked++;
                  }
                } else {
                  // release the resources
                  readyQueue[taskNumber] = 0;
                  blockedQueue[numberBlocked] = taskNumber;
                  numberBlocked++;
                }
              } else {
                // release the resources
                readyQueue[taskNumber] = 0;
                blockedQueue[numberBlocked] = taskNumber;
                numberBlocked++;
              }
            } else 
              delay[activityIndex]--;
          } else if (activ == RELEASE) {
            // delay counter
            int delayTime = delay[activityIndex];

            if (delayTime == 0) {		
              // assumes enough to release no errors
              int requestResourceNum = resourceType[activityIndex]-1;
              int numResources = numResourceUnits[activityIndex];

              // subtract amount of resources from the task
              resourcesRequested[taskNumber][requestResourceNum] -= numResources;

              // add back to resource unit pool
              tempResourcesRemaining[requestResourceNum] += numResources;
              findNextLocationForTask(taskNumber);
            } else 
              delay[activityIndex]--;
          } else if (activ == TERMINATE) {
            // delay counter
            int delayTime = delay[activityIndex];

            if (delayTime == 0) {		
              // release all resources
              for (i = 0; i < numResourcesFinal; i++)
                tempResourcesRemaining[i] += resourcesRequested[taskNumber][i];

              numberTerminated++;
              terminateCycle[taskNumber] = cycle;
              activityCurrentIndex[taskNumber] = -1;
            } else 
              delay[activityIndex]--;
          }
        } // end if (taskNumber != -1)
      }

    for (i = 0; i < numResourcesFinal; i++)
      resourceUnitsRemaining[i] = tempResourcesRemaining[i];

    // increment cycle
    if (deadlockBool < 1)
      cycle++;
  }
}

/**
 * FIFO Algorithm Complete
 */
void runFIFO() {
  int cycle = 0;
  int blockIndex, readyIndex;
  int deadlockBool = 0;

  while (numberTerminated != numTasks) {
    int i;
    // =)
    int skipAry[50];
    for(i=0;i<numTasks;i++)
      skipAry[i] = 0;
    // ---- =)

    // <!-- start, the temporary amount of resources remaining
    int tempResourcesRemaining[50];
    for(i=0;i<numResourcesFinal;i++) 
      tempResourcesRemaining[i] = resourceUnitsRemaining[i];
    // end -->

    // first check if any blocked and grab lowest blocked task
    int lowestBlockedIndex = 0;
    for(blockIndex=0;blockIndex<numberBlocked;blockIndex++) {
      int taskNumber = blockedQueue[blockIndex];

      // retrieves the lowest blocked task incase of a deadlock
      if(taskNumber < blockedQueue[lowestBlockedIndex] && taskNumber != -1)
        lowestBlockedIndex = blockIndex;

      // meaning if not null 
      if(taskNumber != -1) {
        if(deadlockBool == 0)
          totalBlocks[taskNumber]++;
        // check if able to grant request
        int activityIndex = activityCurrentIndex[taskNumber];

        int requestResourceNum = resourceType[activityIndex]-1;
        int numResources = numResourceUnits[activityIndex];

        if(tempResourcesRemaining[requestResourceNum] >= numResources) {
          // check if amount of resources requested < or = to the amount allowed
          if(resourcesForTask[taskNumber][requestResourceNum] >= numResources) {
            if(resourceUnitsRemaining[requestResourceNum] >= numResources) {
              // decrement total remaining
              tempResourcesRemaining[requestResourceNum] -= numResources;

              // assign the resources
              resourcesRequested[taskNumber][requestResourceNum] += numResources;
              findNextLocationForTask(taskNumber);
              blockedQueue[blockIndex] = -1;
              readyQueue[taskNumber] = 1;
              deadlockBool = 0;
              skipAry[taskNumber] = 1;
            } 
          } 
        }
      }
    }

    // deadlocked
    if(numberBlocked == numTasks-numberTerminated) {
      int lowestBlockedTask = blockedQueue[lowestBlockedIndex];

      if(lowestBlockedTask != -1) {
        // remove lowest task number
        // loop through each blocked and grab lowest
        numberTerminated++;

        // releases the resource units
        for(i=0;i<numResourcesFinal;i++) {
          int numResources = resourcesRequested[lowestBlockedTask][i];

          tempResourcesRemaining[i] += numResources;
        }
        terminateCycle[lowestBlockedTask] = -1;
        blockedQueue[lowestBlockedIndex] = -1;
        deadlockBool++;
      }
    }

    // ?
    int tempNumBlocked = 0;
    for(i=0;i<numberBlocked;i++) {
      if(blockedQueue[i] == -1) {
        int j;
        for(j=i;j<numberBlocked;j++) 
          blockedQueue[j] = blockedQueue[j+1];
        tempNumBlocked++;
      }
    }
    numberBlocked -= tempNumBlocked;
    // ?

    if(deadlockBool == 0)
      for(readyIndex=0;readyIndex<numTasks;readyIndex++) {
        // the array's task number location
        if(readyQueue[readyIndex] == 1 && skipAry[readyIndex] != 1) {
          int taskNumber = readyIndex;

          // grab activityIndex
          int activityIndex = activityCurrentIndex[taskNumber];

          // grab activity
          int activ = activity[activityIndex];

          if(activ == INITIATE) {
            resourcesForTask[taskNumber][resourceType[activityIndex]-1] = numResourceUnits[activityIndex];
            //activityCurrentIndex[taskNumber]++;
            findNextLocationForTask(taskNumber);
          } else if(activ == REQUEST) {
            // delay counter
            int delayTime = delay[activityIndex];

            if(delayTime == 0) {		
              // check if there is enough, if there is subtract and add
              int requestResourceNum = resourceType[activityIndex]-1;
              int numResources = numResourceUnits[activityIndex];

              // if the amount of resources avaliable is > or = then the amount being requested
              if(tempResourcesRemaining[requestResourceNum] >= numResources) {
                // check if amount of resources requested < or = to the amount allowed
                if(resourcesForTask[taskNumber][requestResourceNum] >= numResources) {
                  if(resourceUnitsRemaining[requestResourceNum] >= numResources) {
                    // decrement total remaining
                    tempResourcesRemaining[requestResourceNum] -= numResources;

                    // assign the resources
                    resourcesRequested[taskNumber][requestResourceNum] += numResources;
                    findNextLocationForTask(taskNumber);
                  } else {
                    readyQueue[taskNumber] = 0;
                    blockedQueue[numberBlocked] = taskNumber;
                    numberBlocked++;
                  }
                } else {
                  // release the resources
                  readyQueue[taskNumber] = 0;
                  blockedQueue[numberBlocked] = taskNumber;
                  numberBlocked++;
                }
              } else {
                // release the resources
                readyQueue[taskNumber] = 0;
                blockedQueue[numberBlocked] = taskNumber;
                numberBlocked++;
              }
            } else 
              delay[activityIndex]--;
          } else if(activ == RELEASE) {
            // delay counter
            int delayTime = delay[activityIndex];

            if(delayTime == 0) {		
              // assumes enough to release no errors
              int requestResourceNum = resourceType[activityIndex]-1;
              int numResources = numResourceUnits[activityIndex];

              // subtract amount of resources from the task
              resourcesRequested[taskNumber][requestResourceNum] -= numResources;

              // add back to resource unit pool
              tempResourcesRemaining[requestResourceNum] += numResources;
              findNextLocationForTask(taskNumber);
            } else 
              delay[activityIndex]--;
          } else if(activ == TERMINATE) {
            // delay counter
            int delayTime = delay[activityIndex];

            if(delayTime == 0) {		
              // release all resources
              for(i=0;i<numResourcesFinal;i++)
                tempResourcesRemaining[i] += resourcesRequested[taskNumber][i];

              numberTerminated++;
              terminateCycle[taskNumber] = cycle;
              activityCurrentIndex[taskNumber] = -1;
            } else 
              delay[activityIndex]--;
          }
        } // end if (taskNumber != -1)
      }

    for(i=0;i<numResourcesFinal;i++)
      resourceUnitsRemaining[i] = tempResourcesRemaining[i];

    // increment cycle
    if(deadlockBool < 1)
      cycle++;
  }
}

/**
 * This function loops through the input file and parses the data
 * into arrays.
 */
void parseDataFile() {
  // index of data array
  int i=0;

  // temporary string
  char str[20];

  // string index
  int strIndex = 0;

  // this boolean is used to check for blank spaces and new lines
  // when it is on it tells the string to keep copying characters
  // until another space is hit
  int startBool = 0;

  // booleans
  int numTaskBool = 0;
  int numResourcesBool = 0;
  int numResourceTaskCounter = 0;
  int finalCycleBool = -1;

  int activityCounter = 0;
  int activitySwitch = 0;

  while (fileData[i] != 0) {
    // first converts all the return carriages and new lines to spaces
    if (fileData[i] == '\n' || fileData[i] == '\r')
      fileData[i] = ' ';

    // checks for the first non white space, if it is detected it is telling
    // the temporary string to start recording characters until it hits a blank
    // character again
    if (fileData[i] != ' ') 
      startBool = 1;

    // if startBool is true and next character in fileData array is a space then
    // turn the bool to false and place a stopper at the end of the array, resets 
    // the temporary string index
    if (startBool == 1 && fileData[i] == ' ') {
      startBool = 0;
      str[strIndex] = 0;
      strIndex = 0;

      // <!-- start initial declarations
      // if number of tasks are not set, then set it
      // do the same with number of resources
      if (numTaskBool == 0) {
        numTasks = atoi(str);
        numTaskBool = 1;
      } else if (numResourcesBool == 0) {
        numResourcesFinal = atoi(str);
        numResourcesBool = 1;
      } else if (numResourceTaskCounter != numResourcesFinal) {
        // if the counter does not equal to the amount of resources keep looping
        // until all of the resources in the array is filled
        resourceUnits[numResourceTaskCounter] = atoi(str);
        resourceUnitsRemaining[numResourceTaskCounter] = resourceUnits[numResourceTaskCounter];

        numResourceTaskCounter++;		
        finalCycleBool = 0;
      }
      // initial declarations are complete -->

      // <!-- start adding in activity cycles
      if (finalCycleBool == 1) {
        switch (activitySwitch) {
          case 0: taskNumber[activityCounter] = atoi(str); break;
          case 1: delay[activityCounter] = atoi(str); break;
          case 2: activity[activityCounter] = activityToInt(str); break; 
          case 3: resourceType[activityCounter] = atoi(str); break;
          case 4: numResourceUnits[activityCounter] = atoi(str); break;
        }

        activitySwitch++;
        if (activitySwitch > 4) { 
          activitySwitch = 0;

          // counter for array
          activityCounter++;
        }
      }
      // -->

      // this is initalized after the resource tasks have been set
      if (numResourceTaskCounter == numResourcesFinal && finalCycleBool == 0) 
        finalCycleBool = 1;
    }

    // if startBool is on then add the character onto the string and increment
    if (startBool == 1) {
      str[strIndex] = fileData[i];
      strIndex++;
    }

    i++;
  }

  // stores the number of activites in the number of activities global variable
  numActivities = activityCounter;

  // number of tasks ready to be intialized
  for (i = 0; i < numTasks; i++)
    readyQueue[i] = 1;
}

/**
 * This function converts the string forms of the activities
 * into integer values so it is easier to manipulate.
 */
int activityToInt(char *str) {
  char *initStr = "initiate";
  char *requestStr = "request";
  char *releaseStr = "release";
  char *terminateStr = "terminate";

  if (strcmp(str,initStr) == 0) return INITIATE;
  else i f(strcmp(str,requestStr) == 0) return REQUEST;
  else i f(strcmp(str,releaseStr) == 0) return RELEASE;
  else i f(strcmp(str,terminateStr) == 0) return TERMINATE;
}

/** 
 * This method reads in the file into fileData array. 
 */
int readFile(char file[]) {
  fileName = file;

  // checks if the file name exists
  FILE *textFile;
  textFile = fopen(fileName, 'r');
  if (textFile == NULL) {
    printf("This file does not exist.\n");
    return 0;
  }
  else {
    // if the file exists then read each character into
    // the fileData array
    int i = 0;
    while (!feof(textFile)) {
      fscanf(textFile, "%c", &fileData[i]);
      i++;
    }
    // simple hack to make it so that files with or without a \n
    // still parse
    if (fileData[i-2] != '\n') {
      fileData[i] = '\n';
      i++;
    }
    fileData[i] = 0;
    fclose(textFile);
    return 1;
  }
}
