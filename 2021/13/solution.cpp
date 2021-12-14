// compile: g++ -std=c++20 solution.cpp -o solution
// run: ./solution

#include <string>
#include <fstream>
#include <sstream>
#include <iostream>
#include <vector>

using namespace std;

vector<string> split(const string& s, char delimiter) {
   vector<string> tokens;
   string token;
   istringstream tokenStream(s);
   while (getline(tokenStream, token, delimiter)) {
      tokens.push_back(token);
   }
   return tokens;
}

int getMaxValue(vector<string> points, int pointIndex) {
    vector<int> xValues;
    transform(points.begin(), points.end(), back_inserter(xValues),
        [pointIndex](string line){
            auto point = split(line, ',');
            return stoi(point[pointIndex]);
        }
    );
    return *max_element(xValues.begin(), xValues.end());
}

int main(int argc, char *argv[]) {

    // read file
    ifstream file(argv[1]);
    stringstream input;
    input << file.rdbuf();
    auto lines = split(input.str(), '\n');

    // partition points and fold commands
    auto gap = find(lines.begin(), lines.end(), "");
    vector<string> points(lines.begin(), gap);
    vector<string> foldInstructions(gap + 1, lines.end());

    // get paper dimension
    auto width = getMaxValue(points, 0) + 1;
    auto height = getMaxValue(points, 1) + 1;

    // create paper
    bool paper[width][height];
    fill(&paper[0][0], &paper[0][0] + width * height, false);
    for(const auto& point: points) {
        auto splitResult = split(point, ',');
        auto x = stoi(splitResult[0]);
        auto y = stoi(splitResult[1]);
        paper[x][y] = true;
    }

    // run instructions
    for(const auto& instruction: foldInstructions) {
        auto splitResult = split(instruction, '=');
        auto dimen = splitResult[0].back();
        auto value = stoi(splitResult[1]);
        if (dimen == 'x') {
            // fold left
            for(int x = value + 1; x < width; x++) {
                for(int y = 0; y < height; y++) {
                    if (paper[x][y]) {
                        paper[x][y] = false;
                        paper[value * 2 - x][y] = true;
                    }
                }
            }
        } else {
            // fold up
            for(int x = 0; x < width; x++) {
                for(int y = value + 1; y < height; y++) {
                    if (paper[x][y]) {
                        paper[x][y] = false;
                        paper[x][value * 2 - y] = true;
                    }
                }
            }
        }
        // print number of dots
        auto numberOfDots = count(&paper[0][0], &paper[0][0] + width * height, true);
        cout << "Number of dots: " << numberOfDots << endl;
    }

    // get new dimensions
    int newWidth = 0;
    int newHeight = 0;
    for(int x = 0; x < width; x++) {
        for(int y = 0; y < height; y++) {
            if (paper[x][y]) {
                newWidth = x + 1;
                newHeight = y + 1;
            }
        }
    }

    // print paper
    for(int y = 0; y < newHeight; y++) {
        ostringstream stream;
        for(int x = 0; x < newWidth; x++) {
            stream << (paper[x][y] ? '#' : '.');
        }
        cout << stream.str() << endl;
    }
}
