#!/usr/bin/env python3
import os, re, sys

controllers_dir = r"c:\Users\PAULO\OneDrive\Documentos\PlataformaAcademica\ProjetoFinal\plataforma-academica-spring\src\main\java\com\plataforma_academica\plataforma\controller"

for fname in os.listdir(controllers_dir):
    if not fname.endswith(".java"):
        continue
    path = os.path.join(controllers_dir, fname)
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    # Remover blocos de comentários Javadoc que contêm caracteres Unicode ilegais
    # Substituir por comentários simples limpos
    # Vamos simplesmente remover qualquer linha que contenha caracteres como •, ✔, →, etc.
    lines = content.splitlines(keepends=True)
    cleaned_lines = []
    skip_until_end = False
    for line in lines:
        # Se a linha contém caracteres ilegais (bullet, check, arrow, etc.), pular
        if any(ch in line for ch in ["\u2022", "\u2714", "\u2192", "\u2713", "\u2715", "\u2716", "\u2717"]):
            # Se é uma linha de comentário, simplesmente não incluir
            # Se é código, precisamos ser mais cuidadosos - mas esses são só comentários
            continue
        cleaned_lines.append(line)
    cleaned = "".join(cleaned_lines)
    # Corrigir casos onde há @RestController duplicado ou comentários quebrados
    # Remover linhas vazias extras no início de comentários
    cleaned = re.sub(r"(@RestController)\n\s*\n", r"\1\n", cleaned)
    # Remover comentários Javadoc quebrados que começam com texto solto após @RestController
    cleaned = re.sub(r"(@RestController)\n([A-Z][a-zA-Z\s]*)(\n\s*\*\s*)", r"\1\n/**\n * \2\n */\n", cleaned)
    with open(path, "w", encoding="utf-8") as f:
        f.write(cleaned)
    print(f"Limpo: {fname}")
