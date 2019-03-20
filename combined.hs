import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Typeable
import Data.Maybe


legal_tasks = ['A'.. 'H']
legal_machines = ['1'..'8']
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
        let penValid = (length machinePenalIntList) == 8 
        let output_file_n = args !! 1
        if penValid 
            then do 
                let forced = listOfSubsections !! 1
                putStrLn (show forced) 
                let forced_arr = get_filtered_arr forced []
                let checked_tups = [validTuple x 2 | x <- forced_arr]
                let isValid = all_tup_passed checked_tups
                writeToFile output_file_n isValid
                putStrLn (show isValid)
                if isValid 
                    then do
                        let final_forced = [convert_forced x | x <- forced_arr] 
                        -- checks forced forbidden
                        --let forbidden = ["1,A)","(2,B", "(3 ,C)", "(4,,)", "(5,E)"]
                        let forbidden = listOfSubsections !! 2
                        putStrLn (show forbidden)
                        let forbidden_arr = get_filtered_arr forbidden []
                        let checked_tups = [validTuple x 2 | x <- forbidden_arr]
                        let isValid = all_tup_passed checked_tups
                        writeToFile output_file_n isValid
                        putStrLn ("forbidden_check")
                        putStrLn (show isValid)
                        if isValid 
                            then do 
                                let final_forbidden = [convert_forced x | x <- forbidden_arr]    
                                -- checks too near tasks 
                                let too_near = listOfSubsections !! 3 
                                putStrLn (show too_near)
                                let too_near_arr = get_filtered_arr too_near []
                                let checked_tups = [validTooNear x | x <- too_near_arr]
                                let isValid = all_tup_passed checked_tups
                                writeToFile output_file_n isValid
                                if isValid 
                                    then do     
                                        --- checks too near penalties 
                                        let final_too_near_arr = [convert_too_near x | x <- too_near_arr]
                                        let too_near_pen = listOfSubsections !! 5
                                        putStrLn (show too_near_pen) 
                                        let too_near_pen_arr = get_filtered_arr too_near_pen []
                                        let checked_triplets = [validTriplet x | x <- too_near_pen_arr]
                                        let isValid = all_tup_passed checked_triplets
                                        writeToFile output_file_n isValid
                                        if isValid
                                            then do
                                                let final_too_near_pen_arr = [convert_too_near_pen x | x <- too_near_pen_arr] 
                                                -- below is kind of unnessary
                                                writeToFile output_file_n isValid
                                            else do 
                                                exitFailure
                                    else do
                                        exitFailure
                            else do 
                                exitFailure 
                    else do
                        exitFailure
                
            else do
                writeToFile output_file_n penValid
                exitFailure 
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
-- MB function defiintions 

writeToFile file_name valid = do
    fileToWrite <- openFile file_name WriteMode
    if valid then
        hPutStrLn fileToWrite("")
    else
        hPutStrLn fileToWrite("Error while parsing")
    hClose fileToWrite


all_tup_passed a_bool_list =
    if False ` elem ` (a_bool_list)
        then False
        else True 

validTuple :: String -> Int -> Bool 
validTuple str len = 
    (thereAreBrackets str) && (isLen5 str) && (str !! 2 == ',')
    && ((str !! 1) ` elem ` legal_machines) && ((str !! 3 ) ` elem ` legal_tasks)

isLen5 :: String -> Bool 
isLen5 str = length (str) == 5

thereAreBrackets [ ] = False 
thereAreBrackets str = (head str) == '(' && (last str) == ')'


validTooNear :: String -> Bool
validTooNear str = 
    (thereAreBrackets str) && (isLen5 str) && (str !! 2 == ',')
    && ((str !! 1) ` elem ` legal_tasks) && ((str !! 3 ) ` elem ` legal_tasks)


validTriplet :: String -> Bool
validTriplet str = 
    (thereAreBrackets str) && (str !! 2 == ',') && (str !! 4 ==',') && 
    -- ((str !! 1) ` elem ` legal_tasks) && ((str !! 3) ` elem ` legal_tasks) && (machinePenalRowValid ([str !! 5] : []) ) && 
         ((str !! 1) ` elem ` legal_tasks) && ((str !! 3) ` elem ` legal_tasks) && (machinePenalRowValid (try_conversion str) )
    --((str !! 1) ` elem ` legal_tasks) && ((str !! 3) ` elem ` legal_tasks) && (machinePenalRowValid ([(init (((convert_tup_toL (splitAt 5 str)) !! 1))] : []) )


-- need to get everything until the first non empty
-- then need to check everything after empty
------ if everything after is also empty, we're fine. Return 
------ if there is something non empty after, insert it 

--get_filtered_arr :: [] -> [] -> []
--get_filtered_arr [ ] new_l = [-1]

-- need to check everything following the first empty string to ensure 
-- everything else is empty
check_for_more_empty_s [ ] new_l = new_l
check_for_more_empty_s orig new_l= 
    if (length orig == 0) then
        new_l
    else if (head orig == "") then
        check_for_more_empty_s (tail orig) new_l 
    else
        ["?!"]


-- takes a list of strings
-- returns a new list containing only non empty strings if the list is one of 
-- a) a list with all empty strings 
-- b) a list with non empty strings, followed by ALL empty strings
-- c) a list with non empty strings
--  otherwise it returns the list [?!]
get_filtered_arr orig new_l = 
    if length orig == 0 then 
        new_l
    else
        if (head orig /= "" ) then
            get_filtered_arr (tail orig) (new_l ++ [head orig])
        else
            check_for_more_empty_s orig new_l


convert_tup_toL :: (a,a) -> [a]
convert_tup_toL (x,y) = [x,y]



-- this gets us the "number" portion from a too near task penlaty 
-- (init ((convert_tup_toL (splitAt 5 "(A,B,77)")) !! 1)) 
-- [(init ((convert_tup_toL (splitAt 5 str )) !! 1))]
try_conversion str = 
    [(init ((convert_tup_toL (splitAt 5 str )) !! 1))]

try_conversion_no_arr str = 
    (init ((convert_tup_toL (splitAt 5 str )) !! 1))
convert_forced :: String -> (Int, Char)
convert_forced str =
    (digitToInt (str !! 1), str !! 3)


convert_too_near :: String -> (Char,Char)
convert_too_near str = 
    (str !! 1, str !! 3)

convert_too_near_pen :: String -> (Char, Char, Integer)
convert_too_near_pen str = 
    (str !! 1, str !! 3, read (try_conversion_no_arr  str) :: Integer )