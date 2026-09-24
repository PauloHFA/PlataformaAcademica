import { Pipe, PipeTransform } from '@angular/core';
import * as marked from 'marked';
import hljs from 'highlight.js';

@Pipe({
  name: 'markdown',
  standalone: true
})
export class MarkdownPipe implements PipeTransform {
  private markedInstance: marked.Marked;

  constructor() {
    const renderer = new marked.Renderer();
    renderer.code = (token: { text: string; lang?: string; escaped?: boolean }) => {
      const language = token.lang || 'plaintext';
      const validLang = hljs.getLanguage(language) ? language : 'plaintext';
      const highlighted = hljs.highlight(token.text, { language: validLang, ignoreIllegals: true }).value;
      return `<pre><code class="hljs ${validLang}">${highlighted}</code></pre>`;
    };

    this.markedInstance = new marked.Marked({ renderer: renderer, async: false });
  }

  transform(value: string): string {
    if (!value) {
      return '';
    }
    return this.markedInstance.parse(value) as string;
  }
}