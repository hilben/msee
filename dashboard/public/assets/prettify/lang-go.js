PR.registerLangHandler(PR.createSimpleLexer([["pln",/^[\t\n\r \xa0]+/,null,"	\n\r ??"],["pln",/^(?:"(?:[^"\\]|\\[\S\s])*(?:"|$)|'(?:[^'\\]|\\[\S\s])+(?:'|$)|`[^`]*(?:`|$))/,null,"\"'"]],[["com",/^(?:\/\/[^\n\r]*|\/\*[\S\s]*?\*\/)/],["pln",/^(?:[^"'/`]|\/(?![*/]))+/]]),["go"]);