import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Typeable
import Data.Maybe

-- forced = [(1,'A')]
-- forbidden = [(2, 'B')]
-- tooNear = [('H', 'C')]
-- machPens = [[1,10,10,10,10,10,10,10],[10,1,10,10,10,10,10,10],[10,10,1,10,10,10,10,10],[10,10,10,1,10,10,10,10],[10,10,10,10,1,10,10,10],[10,10,10,10,10,1,10,10],[10,10,10,10,10,10,1,10],[10,10,10,10,10,10,10,1]]
-- tooNearPens = [('F', 'G', 100)]
legal_tasks = ['A'.. 'H']
legal_machines = ['1'..'8']


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

        let untext = lines content
        let text = stripWhiteY untext

        let headerIndices = headerIndexList headers text 

        let validHeaders = headerValidity headerIndices
        let output_file_n = args !! 1
        if not(validHeaders)
            then do
                writeToFile output_file_n False "Error while parsing input file"
        else do 

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
 
            let machPens = convertPenalToInt machinePenalties
            putStrLn ("Machine Array:")
            putStrLn (show machPens)
            if (checkNaturalNumbers machPens)
                then do
                let penValid = (length machPens) == 8 

                if penValid 
                    then do 
                        let forced = listOfSubsections !! 1
                        let forced_arr = get_filtered_arr forced []
                        
                        ---
                        --

                        -- 

                        --let checked_tups = [validTuple x 2 | x <- forced_arr]
                        let isValid = check_empty_and_valid_forced forced_arr 
                        writeToFile output_file_n isValid "partial assignment error"
                        if isValid 
                            then do
                                let forced = [convert_forced x | x <- forced_arr] 
                                putStrLn ("Parsed Forced Partial assignments Array:")
                                putStrLn (show forced)
                                -- checks forced forbidden
                                --let forbidden = ["1,A)","(2,B", "(3 ,C)", "(4,,)", "(5,E)"]
                                let forbidden = listOfSubsections !! 2
                                let forbidden_arr = get_filtered_arr forbidden []
                                let isValid = check_empty_and_valid_forced forbidden_arr 
                                writeToFile output_file_n isValid "invalid machine/task"
                                if isValid 
                                    then do 
                                        let forbidden = [convert_forced x | x <- forbidden_arr]
                                        putStrLn ("Parsed Forbidden Machine Array:")
                                        putStrLn (show forbidden)
                                        -- checks too near tasks 
                                        let too_near = listOfSubsections !! 3 
                                        let too_near_arr = get_filtered_arr too_near []
                                        let isValid = check_empty_and_valid_too_near too_near_arr
                                        writeToFile output_file_n isValid "Error while parsing input file"
                                        if isValid 
                                            then do     
                                                --- checks too near penalties 
                                                let tooNear = [convert_too_near x | x <- too_near_arr]
                                                putStrLn ("Parsed too-near tasks Array:")
                                                putStrLn (show tooNear)
                                                let too_near_pen = listOfSubsections !! 5

                                                let too_near_pen_arr = get_filtered_arr too_near_pen []
                                                let tooNearPens = [convert_too_near_pen x | x <- too_near_pen_arr]
                                                let isValid = check_empty_and_valid_too_near_pen too_near_pen_arr 
                                                writeToFile output_file_n isValid "invalid task"
                                                if isValid
                                                    then do
                                                         
                                                        putStrLn ("Parsed too-near penalties Array:")
                                                        putStrLn (show tooNearPens)
                                                        -- below is kind of unnessary
                                                        writeToFile output_file_n isValid "invalid task"
                                                        let all = permutations "ABCDEFGH"
                                                        let forcedAcc = getFAccepted forced forbidden all []
                                                        let allAccepted = getTooNearAccepted tooNear forcedAcc []
                                                        --print tooNearAccepted
                                                        --findBestMatch 
                                                        let bestMatch = findBestMatch allAccepted tooNearPens machPens 2147483647 []
                                                        writeToFile output_file_n False (printOutput bestMatch)
                                                        -- let machinePenalties = getMachinePenalties machPens "ABCDEFGH" 0
                                                        -- let tooNearPenalties = getTooNearPenalties tooNearPens "ABCDEFGH" machinePenalties
                                                        -- print tooNearPenalties


                                                        -- machine = “ABCDEFGH”

                                                        -- findIndex1 :: [(Integer, Char)] -> [Char] -> Integer -> Integer
                                                        -- findIndex1 tuple [] index = -1
                                                        -- findIndex1 tuple (x:xs) index 
                                                        --     | get2nd(tuple) == x = index
                                                        --     | otherwise = findIndex1 tuple xs (index + 1)

                                                        
                                                    else do 
                                                        if not ((validTaskTup too_near_pen_arr 1) && (validTaskTup too_near_pen_arr 3))
                                                            then do 
                                                                writeToFile output_file_n isValid "invalid task"
                                                            else do 
                                                                writeToFile output_file_n isValid "invalid penalty"
                                                        exitFailure
                                            else do
                                                exitFailure
                                    else do 
                                        exitFailure 
                            else do
                                exitFailure
                        
                    else do
                        writeToFile output_file_n penValid "machine penalty error"
                        exitFailure 
                else do
                    writeToFile output_file_n False "invalid penalty"
                    exitFailure 

    

-------------------------------------------------------------------------------------------------

printOutput x = "Solution " ++ (fst x) ++ "; Quality: " ++ (show (snd x)) ++ "\n"

stringIsAccepted :: [(Int, Char)] -> [Char] -> Bool
stringIsAccepted [] machine = True
stringIsAccepted (x:xs) machine = (((elemIndices(snd(x)) machine) == [fst(x) -1]) && stringIsAccepted xs machine)

stringIsNotForbidden  :: [(Int, Char)] -> [Char]-> Bool
stringIsNotForbidden [] machine = True
stringIsNotForbidden (x:xs) machine = (((elemIndices(snd(x)) machine) /= [fst(x) -1]) && stringIsNotForbidden xs machine)


-- Takes in the forced penalties, the forbidden, the set of all possible strings, the set of all allowed strings so far, and returns a set of all allowed strings
getFAccepted :: [(Int, Char)] -> [(Int, Char)] -> [[Char]] -> [[Char]] -> [[Char]]

getFAccepted forced forbidden [] accepted = accepted

getFAccepted forced forbidden (y:ys) accepted
    | (stringIsAccepted forced y) && (stringIsNotForbidden forbidden y) = getFAccepted forced forbidden ys (accepted ++ [y])
    | otherwise = getFAccepted forced forbidden ys accepted

-- Takes in too near tasks and machine string, returns bool.
stringTooNear :: [(Char, Char)] -> [Char] -> Bool
stringTooNear [] machine = True
--Checks if indices are 7 and 0, checks if indices are x and x-1, checks rest of too near
stringTooNear (x:xs) machine = (stringTooNear xs machine) && not (elemIndices(fst(x)) machine == [7] && elemIndices(snd(x)) machine == [0]) && not (elemIndices(fst(x)) machine!!0 == (elemIndices(snd(x)) machine!!0)-1)


-- Takes in the too near tasks, the set of all possible forced strings, the set of all allowed too near tasks and returns the set of all allowed strings recursively
getTooNearAccepted :: [(Char, Char)] -> [[Char]] -> [[Char]] -> [[Char]]
getTooNearAccepted [] (y:ys) accepted = accepted
getTooNearAccepted (x:xs) [] accepted = accepted
getTooNearAccepted (x:xs) (y:ys) accepted
    | stringTooNear (x:xs) y = getTooNearAccepted (x:xs) ys accepted ++ [y]
    | otherwise = getTooNearAccepted (x:xs) ys accepted


--takes in a 2D array of integers, the machine string, and the total penalty so far and returns the total machine penalty
getMachinePenalties :: [[Int]] -> [Char] -> Int -> Int
stringRef = "ABCDEFGH"
getMachinePenalties matrix [] pen = 0
getMachinePenalties matrix (y:ys) pen = (getMachinePenalties matrix ys pen) + (matrix!!(8 - length (y:ys)))!!((elemIndices y stringRef)!!0)


extractFirst :: (a, b, c) -> a
extractFirst (a,_,_) = a

extractSecond :: (a, b, c) -> b
extractSecond (_,b,_) = b

extractThird :: (a, b, c) -> c
extractThird (_,_,c) = c


getTooNearPenalties :: [(Char, Char, Int)] -> [Char] -> Int -> Int
getTooNearPenalties [] machine pen = pen
getTooNearPenalties (x:xs) machine pen
    | (elemIndices(extractFirst(x)) machine == [7] && elemIndices(extractSecond(x)) machine == [0]) = (getTooNearPenalties xs machine pen) + extractThird(x)
    | (elemIndices(extractFirst(x)) machine!!0 == (elemIndices(extractSecond(x)) machine!!0)-1) = (getTooNearPenalties xs machine pen) + extractThird(x)
    | otherwise = getTooNearPenalties xs machine pen


findBestMatch :: [[Char]] -> [(Char, Char, Int)] -> [[Int]] -> Int -> [Char] ->([Char],Int)
findBestMatch [] tooNearPenalties matrix tot best = (best,tot)
findBestMatch (x:xs) tooNearPenalties matrix tot best
    | getTooNearPenalties tooNearPenalties x (getMachinePenalties matrix x 0) <= tot = findBestMatch xs tooNearPenalties matrix (getTooNearPenalties tooNearPenalties x (getMachinePenalties matrix x 0)) x
    | otherwise = findBestMatch xs tooNearPenalties matrix tot best
	

----------------------------------------------------------------------------Start of 'machine penalty parsing' function definitions-------------------------------------------     

-- Returns a list of lists of type Int. 
-- Removes a row of penalties if it is of an invalid form.
-- Therefore to check for machine penalty validity just check that this
-- resulting list of lists is of length 8. If length 8, then all penalties are valid
convertPenalToInt :: [String] -> [[Int]]
convertPenalToInt [] = []
convertPenalToInt (x:xs) 
    |(xs /= []) && (length y == 8) = stringListToIntList y: convertPenalToInt xs
    |(xs == []) && (length y == 8) = stringListToIntList y:convertPenalToInt xs
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
-- && (fromMaybe (0) y `mod` 1 == 0)
machinePenalRowValid :: [String] -> Bool
machinePenalRowValid [] = True
machinePenalRowValid (x:xs) 
    |(y /= Nothing ) && (fromMaybe (0) y >= 0) && (xs /= []) = machinePenalRowValid xs
    |(y /= Nothing) && (xs == []) = True
    |(y == Nothing) = False
    |otherwise = False
    where y = readMaybe x :: Maybe Int

-- Takes a string and either returns a Just type of that value or Nothing.
readMaybe :: Read a=> String -> Maybe a
readMaybe s = case reads s of
                [(val, "")] -> Just val
                _           -> Nothing

checkNaturalNumbers :: [[Int]] -> Bool
checkNaturalNumbers [] = True
checkNaturalNumbers (x:xs)
    |(not (-1 `elem` x)) = checkNaturalNumbers xs
    | otherwise = False

----------------------------------------------------------------------------End of 'machine penalty parsing' function definitions----------------------------------------------
----------------------------------------------------------------------------Start of 'sectioning and storing input file data' function defintions------------------------------
checkForEmpty :: [[Char]] -> Bool 
checkForEmpty [ ] = True 
checkForEmpty arr = 
    if head arr == " "
        then 
            checkForEmpty (tail arr)
        else 
            False 

-- first checks to see whether the filtered ("" removed arr) is empty, returns true
-- then checks to see whetehr the filtered arr only contains " " strings. If so, it returns True 
-- otherwise it checks to see whether the is valid based on what wey are checking for 
check_empty_and_valid_forced :: [[Char]] -> Bool 
check_empty_and_valid_forced some_arr = 
    if ((length some_arr)) == 0 || (checkForEmpty some_arr) 
        then do
            True
        else do
            let checked_tups = [validTuple x 2 | x <- some_arr]
            all_tup_passed checked_tups

check_empty_and_valid_too_near :: [[Char]] -> Bool 
check_empty_and_valid_too_near some_arr = 
    if ((length some_arr)) == 0 || (checkForEmpty some_arr)
        then do
            True
        else do 
            let checked_tups = [validTooNear x | x <- some_arr]
            all_tup_passed checked_tups 


check_empty_and_valid_too_near_pen :: [[Char]] -> Bool 
check_empty_and_valid_too_near_pen some_arr = 
    if ((length some_arr)) == 0 || (checkForEmpty some_arr)
        then do
            True
        else do 
            let checked_triplets = [validTriplet x | x <- some_arr]
            all_tup_passed checked_triplets  



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

stripWhiteY :: [String] -> [String]
stripWhiteY [] = []
stripWhiteY (x:xs) = [stripWhiteX x] ++ stripWhiteY xs

stripWhiteX :: [Char] -> [Char]
stripWhiteX [] = []
stripWhiteX (x:xs)
   | x == ' ' = stripWhiteX xs
   | otherwise = (x:xs)
    
----------------------------------------------------------------------------End of 'sectioning and storing input file data' function defintions----------------------------------------------
-- MB function defiintions 

writeToFile file_name valid  errorName = do
    fileToWrite <- openFile file_name WriteMode
    if valid then
        hPutStrLn fileToWrite("")
    else
        hPutStrLn fileToWrite(errorName)
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
         ((str !! 1) ` elem ` legal_tasks) && ((str !! 3) ` elem ` legal_tasks) && (machinePenalRowValid (try_conversion str))
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

convert_too_near_pen :: String -> (Char, Char, Int)
convert_too_near_pen str = 
    (str !! 1, str !! 3, read (try_conversion_no_arr  str) :: Int )

validTaskTup :: [String] -> Int -> Bool 
validTaskTup some_l index =
    not (False ` elem ` [(x !! index ` elem ` legal_tasks ) | x <- some_l])
