import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Typeable
import Data.Maybe


-- An example implementation of functions defined below
main = do
    args <- getArgs
    -- If CLI arguments are in wring format exit
    if length(args) /= 2 then do
        putStrLn("usage: <input file> <output file>")
        exitFailure
    else do
        content <- readFile (head args)
        -- Put input.txt (arg[0]) into [String]
        let headers = ["Name:", "forced partial assignment:", "forbidden machine:", "too-near tasks:", "machine penalties:", "too-near penalities"]

        let text = lines content
        let headerIndices = headerIndexList headers text 

        -- listOfSubsections is the list of all [String]'s. and to access each subsection of data just access each index as so: 
        let listOfSubsections = getSubsections headerIndices text
        -- listOfSubsections!!0 returns: name list
        -- listOfSubsections!!1 returns: partial assignments list
        -- listOfSubsections!!2 returns: forbidden machines list
        -- listOfSubsections!!3 returns: too-near tasks list
        -- listOfSubsections!!4 returns: machine penalties list
        -- listOfSubsections!!5 returns: too-near penalties list
        -- let partialAssList = listOfSubsections!!1    (Example of how you would access each section of data)

        -- machinePenalites is the list of strings of each machine penalty row of the form: ["1 1 1 1 1 1 1 1",...,"1 1 1 1 1 1 1 1"]
        let machinePenalties = listOfSubsections!!4
        -- Calling convertPenalToInt will convert the above list of string to a list of lists of integers of the form: [[1,1,1,1,1,1,1,1],...,[1,1,1,1,1,1,1,1]]
        -- **IMPORTANT** If an invalid value is detected in the machine penalties data, the entire row in which that value is located will be removed from the 
        -- returned value below. Therefore, to check for machine penalty validity just compare the length of the returned Integer list below to 8; If == 8: MachPen are valid
        -- Else: Quit and write to output.txt "machine penalty error"
        let machinePenalIntList = convertPenalToInt machinePenalties
        --putStrLn $ show (machinePenalIntList)
----------------------------------------------------------------------------Start of 'machine penalty parsing' function definitions-------------------------------------------     

-- Returns a list of lists of type Int. 
-- Removes a row of penalties if it is of an invalid form.
-- Therefore to check for machine penalty validity just check that this
-- resulting list of lists is of length 8. If length 8, then all penalties are valid
convertPenalToInt :: [String] -> [[Int]]
convertPenalToInt [] = []
convertPenalToInt (x:xs)
    |(machinePenalRowValid y) && (xs /= []) && (length y == 8) = stringListToIntList y: convertPenalToInt xs
    |(machinePenalRowValid y) && (xs == []) && (length y == 8) = stringListToIntList y:convertPenalToInt xs
    |otherwise = convertPenalToInt (xs)
    where y = stringToStringList x

-- Converts [String] to [Int]
stringListToIntList :: [String] -> [Int]
stringListToIntList [] = []
stringListToIntList (x:xs) = fromMaybe (-1) y:stringListToIntList xs
    where y = readMaybe x :: Maybe Int

-- Converts String to [String]
stringToStringList :: String -> [String]
stringToStringList "" = []
stringToStringList str = words(str)
    
-- Converts all string indices in string array to Ints and checks that they are >= 0 and natural numbers.
-- Returns true if whole row is valid
-- False if at least one is invalid
machinePenalRowValid :: [String] -> Bool
machinePenalRowValid [] = True
machinePenalRowValid (x:xs)    
    |(y /= Nothing ) && (fromMaybe (0) y >= 0) && (fromMaybe (0) y `mod` 1 == 0) && (xs /= []) = machinePenalRowValid xs
    |(y /= Nothing) && (xs == []) = True
    |(y == Nothing) = False
    |otherwise = False
    where y = readMaybe x :: Maybe Int

-- Takes a string and either returns a Just type of that value or Nothing.
readMaybe :: Read a=> String -> Maybe a
readMaybe s = case reads s of
                [(val, "")] -> Just val
                _           -> Nothing

----------------------------------------------------------------------------End of 'machine penalty parsing' function definitions----------------------------------------------
----------------------------------------------------------------------------Start of 'sectioning and storing input file data' function defintions------------------------------

-- Chop input.txt file into sublists of data between headers
slice :: Int -> Int -> [String] -> [String]
slice from to xs = take (to - from + 1) (drop from xs)

-- Finds index of all headers in input file string list and append them to a list of their own
-- If the header does not exist in the input file string list then append -1 to the resulting list
headerIndexList :: [String] -> [String] -> [Int]
headerIndexList [] inputFileList = []
headerIndexList (x:xs) inputFileList = (fromMaybe (-1) (elemIndex x inputFileList)):headerIndexList xs inputFileList

-- If -1 is not present in list of header indices then the headers are of a valid form
headerValidity :: [Int] -> Bool
headerValidity headerIndices = not (-1 `elem` headerIndices)

-- Intakes list of header indices and input string list
-- Goes through each index and sections off the strings between header indices into their own lists.
-- These individual lists of strings are appended to a master list containing all input file data.
getSubsections :: [Int] -> [String] -> [[String]]
getSubsections [] inputFileList = []
getSubsections (x:xs) inputFileList 
    | (xs /= []) = slice (x+1) ((head xs)-1) inputFileList:getSubsections xs inputFileList
    | otherwise = slice (x+1) ((length inputFileList)-1) inputFileList:getSubsections xs inputFileList

----------------------------------------------------------------------------End of 'sectioning and storing input file data' function defintions----------------------------------------------
